package com.ailab.ailabsystem.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.ailab.ailabsystem.common.CommonConstant;
import com.ailab.ailabsystem.common.R;
import com.ailab.ailabsystem.common.RedisKey;
import com.ailab.ailabsystem.enums.ResponseStatusEnum;
import com.ailab.ailabsystem.enums.UserStatus;
import com.ailab.ailabsystem.exception.CustomException;
import com.ailab.ailabsystem.mapper.*;
import com.ailab.ailabsystem.model.dto.LoginRequest;
import com.ailab.ailabsystem.model.dto.UserInfoDTO;
import com.ailab.ailabsystem.model.entity.*;
import com.ailab.ailabsystem.model.vo.*;
import com.ailab.ailabsystem.service.UserService;
import com.ailab.ailabsystem.util.RedisOperator;
import com.ailab.ailabsystem.util.RequestUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import springfox.documentation.spring.web.json.Json;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private ProjectMemberMapper projectMemberMapper;

    @Resource
    private ProjectMapper projectMapper;

    @Resource
    private CompetitionSituationMapper competitionSituationMapper;

    @Resource
    private AwardMapper awardMapper;

    @Resource
    private IntranetMapper intranetMapper;

    @Resource
    private CompetitionMapper competitionMapper;

    @Resource
    private RedisOperator redis;

    @Override
    public R<Object> login(LoginRequest loginRequest) {
        //todo 将获取到的密码进行MD5加密处理(先写在这里，之后再考虑)
        String password = loginRequest.getPassword();
        String studentNumber = loginRequest.getStudentNumber();
        User userLogin = userMapper.selectByStuNumAndPwd(studentNumber, password);
        if (ObjectUtil.isNull(userLogin)) {
            throw new CustomException(ResponseStatusEnum.USERNAME_PASSWORD_ERROR);
        }
        // 查看用户状态，如果为已禁用状态，则返回用户已禁用结果
        if (userLogin.getUserStatus() == UserStatus.DISABLE.getCode()) {
            throw new CustomException(ResponseStatusEnum.FORBIDDEN_ERROR);
        }
        // 登录成功，生成随机token
        String token = UUID.randomUUID().toString();
        UserVo userVo = BeanUtil.copyProperties(userLogin, UserVo.class);
        userVo.setNickname(userLogin.getUserInfo().getRealName());
        // 获取redis key
        String loginUserKey = RedisKey.getLoginUserKey(token);
        // 转成json
        String userJson = JSONUtil.toJsonStr(userVo);
        // 存入redis
        redis.set(loginUserKey, userJson);
        HashMap<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("user", userVo);
        return R.success(map);
    }

    @Override
    public Map<String, List<UserInfoVo>> getUserInfoList() {
        // 查缓存
        String userInfoVoListJson = redis.get(RedisKey.getUserInfos());
        List<UserInfoVo> userInfoVos = new ArrayList<>();
        if (StringUtils.isNotBlank(userInfoVoListJson)) {
            userInfoVos = JSONUtil.toList(userInfoVoListJson, UserInfoVo.class);
        } else {
            // 查询所有学生信息
            List<UserInfo> userInfos = userInfoMapper.selectList(null);
            userInfoVos = userInfoToVoList(userInfos);
            redis.set(RedisKey.getUserInfos(), JSONUtil.toJsonStr(userInfoVos), 60 * 5);
        }

        return studentClassification(userInfoVos);
    }

    private Map<String, List<UserInfoVo>> studentClassification(List<UserInfoVo> userInfoVos) {
        // 因为只有两个，分别是本届和往届，所以直接实例化为2
        HashMap<String, List<UserInfoVo>> map = new HashMap<>(2);
        List<UserInfoVo> currentStudents = new ArrayList<>();
        List<UserInfoVo> previousStudents = new ArrayList<>();
        userInfoVos.forEach(userInfoVo -> {
            UserInfo userInfo = userInfoMapper.selectById(userInfoVo.getUserInfoId());
            if (currentOrPrevious(userInfo.getEnrollmentYear())) {
                previousStudents.add(userInfoVo);
            } else {
                // 获取年级
                String stuGrade = getStuGrade(userInfo.getEnrollmentYear());
                userInfoVo.setGrade(stuGrade);
                currentStudents.add(userInfoVo);
            }
        });
        map.put(CommonConstant.CURRENT_STUDENTS, currentStudents);
        map.put(CommonConstant.PREVIOUS_STUDENTS, previousStudents);
        return map;
    }

    /**
     * @author huiyuan
     * @param userInfoVos
     * @return
     */
    private Map<String, List<UserSimpleVo>> studentClassification2(List<UserInfoVo> userInfoVos) {
        HashMap<String, List<UserSimpleVo>> map = new HashMap<>(2);
        List<UserSimpleVo> currentStudents = new ArrayList<>();
        List<UserSimpleVo> previousStudents = new ArrayList<>();
        userInfoVos.forEach(userInfoVo -> {
            UserSimpleVo userSimpleVo = BeanUtil.copyProperties(userInfoVo, UserSimpleVo.class);
            User user = userMapper.selectById(userSimpleVo.getUserId());
            userSimpleVo.setUserRight(user.getUserRight());
            userSimpleVo.setAvatar(user.getAvatar());
            UserInfo userInfo = userInfoMapper.selectById(userInfoVo.getUserInfoId());
            Date enrollmentYear = userInfo.getEnrollmentYear();
            if (currentOrPrevious(enrollmentYear)) {
                previousStudents.add(userSimpleVo);
            } else {
                userSimpleVo.setGrade(getStuGrade(enrollmentYear));
                currentStudents.add(userSimpleVo);
            }
        });
        map.put(CommonConstant.CURRENT_STUDENTS, currentStudents);
        map.put(CommonConstant.PREVIOUS_STUDENTS, previousStudents);
        return map;
    }

    @Override
    public UserInfo getUserInfo(Integer userInfoId) {
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("user_info_id", userInfoId);
        UserInfo userInfo = userInfoMapper.selectOne(wrapper);
        return userInfo;
    }

    @Override
    public UserInfo getUserInfoByUserId(Long userId) {
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        UserInfo userInfo = userInfoMapper.selectOne(wrapper);
        return userInfo;
    }

    @Override
    public R getIndexUserInfo(String loginUserKey) {
        //从redis取出User的信息
        String userJson = redis.get(loginUserKey);
        UserVo userVo = JSONUtil.toBean(userJson, UserVo.class);
        Long userId = userVo.getUserId();

        //注意上面查的是登录用户的信息（User），而下面查的是用户的首页信息（IndexUserInfo）
        //先查redis的首页用户
        String indexUserKey = RedisKey.getIndexUser(userId);
        String resultUserJson = redis.get(indexUserKey);
        if (StrUtil.isNotBlank(resultUserJson)) {
            return R.success(JSONUtil.toBean(resultUserJson, IndexUserVo.class));
        }
        IndexUserVo indexUserVo = setIndexUserVoParams(userVo);

        String indexUserJson = JSONUtil.toJsonStr(indexUserVo);
        redis.set(indexUserKey, indexUserJson, 60 * 60);
        return R.success(indexUserVo);
    }


    @Override
    public String getUserRightName(Integer userRight) {
        if (userRight == null) {
            throw new CustomException(ResponseStatusEnum.NOT_FOUND_ERROR);
        }
        String rightName = "";
        switch (userRight) {
            case 0:
                rightName = "管理员";
                break;
            case 1:
                rightName = "老师";
                break;
            case 2:
                rightName = "实验室负责人";
                break;
            case 3:
                rightName = "实验室成员";
                break;
            case 4:
                rightName = "实验室合作伙伴";
                break;
            case 5:
                rightName = "非实验室成员";
                break;
        }
        return rightName;
    }

    /**
     * @param loginUserKey
     * @return
     * @author huiyuan
     */
    @Override
    public R getInfoOfMe(String loginUserKey) {
        //先从redis获取登录用户的信息
        String userJson = redis.get(loginUserKey);
        if (StrUtil.isBlank(userJson)) {
            throw new CustomException(ResponseStatusEnum.NOT_LOGIN_ERROR);
        }
        User user = JSONUtil.toBean(userJson, User.class);
        Long userId = user.getUserId();
        //查redis看是否存在userInfoVo信息
        String userInfoKey = RedisKey.getUserInfo(userId);
        String userInfoJson = redis.get(userInfoKey);
        UserInfo userInfo = new UserInfo();
        UserInfoVo userInfoVo = new UserInfoVo();
        //不存在则查询数据库
        if (StrUtil.isBlank(userInfoJson)) {
            //注意，这里查询的是原始的userInfo，并非响应给前端的userInfoVo
            userInfo = getUserInfoByUserId(userId);
            //利用userInfo转化为userInfoVo信息
            userInfoVo = getUserInfoVo(userInfo);
            String userInfoVoJsonStr = JSONUtil.toJsonStr(userInfoVo);
            redis.set(userInfoKey, userInfoVoJsonStr, 60 * 30);
        } else {
            //存在则直接把JSON转化为实体类
            userInfoVo = JSONUtil.toBean(userInfoJson, UserInfoVo.class);
        }
        //获取与用户相关的项目
        String userProjectKey = RedisKey.getUserProject(userId);
        String userProjectJson = redis.get(userProjectKey);
        List<ProjectVo> userProjectVos = new ArrayList<>();
        if (StrUtil.isBlank(userProjectJson)) {
            userProjectVos = getUserProjectVos(userInfo.getUserId());
            String userProjectJsonStr = JSONUtil.toJsonStr(userProjectVos);
            redis.set(userProjectKey, userProjectJsonStr, 60 * 30);
        } else {
            userProjectVos = JSONUtil.toList(userProjectJson, ProjectVo.class);
        }
        List<AwardVo> userAwardVos = getUserAwardVos(userId);
        Map<String, Object> map = new HashMap<>();
        map.put("userInfoVo", userInfoVo);
        map.put("userProjectVos", userProjectVos);
        map.put("userAwardVos", userAwardVos);

        return R.success(map);
    }

    @Override
    public R getInfoById(Long userId) {
        //先尝试从redis获取userInfo信息
        String userInfoJson = redis.get(RedisKey.getUserInfo(userId));
        UserInfo userInfo = new UserInfo();
        UserInfoVo userInfoVo = new UserInfoVo();
        if (StrUtil.isBlank(userInfoJson)) {
            QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", userId);
            userInfo = userInfoMapper.selectOne(queryWrapper);
            userInfoVo = getUserInfoVo(userInfo);
            redis.set(RedisKey.getUserInfo(userId), JSONUtil.toJsonStr(userInfoVo), 60 * 30);
        } else {
            //存在则直接把JSON转化为实体类
            userInfoVo = JSONUtil.toBean(userInfoJson, UserInfoVo.class);
        }
        //获取与用户相关的项目
        String userProjectKey = RedisKey.getUserProject(userId);
        String userProjectJson = redis.get(userProjectKey);
        List<ProjectVo> userProjectVos = new ArrayList<>();
        if (StrUtil.isBlank(userProjectJson)) {
            userProjectVos = getUserProjectVos(userId);
            String userProjectJsonStr = JSONUtil.toJsonStr(userProjectVos);
            redis.set(userProjectKey, userProjectJsonStr, 60 * 30);
        } else {
            userProjectVos = JSONUtil.toList(userProjectJson, ProjectVo.class);
        }
        List<AwardVo> userAwardVos = getUserAwardVos(userId);
        Map<String, Object> map = new HashMap<>();
        map.put("userInfoVo", userInfoVo);
        map.put("userProjectVos", userProjectVos);
        map.put("userAwardVos", userAwardVos);

        return R.success(map);
    }

    @Override
    public R updateMyInfo(UserInfoDTO userInfoDTO,  String token) {
        //从数据库中获取userInfo
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInfo::getUserInfoId, userInfoDTO.getUserInfoId());
        UserInfo userInfo = userInfoMapper.selectOne(queryWrapper);
        //因为user_info表是没有avatar的，所以在这里要修改user表的avatar
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("user_id", userInfo.getUserId()).set("avatar", userInfoDTO.getAvatar());
        this.update(updateWrapper);
        BeanUtil.copyProperties(userInfoDTO, userInfo , CopyOptions.create().setIgnoreNullValue(true));
        //修改user_info表的信息
        userInfoMapper.updateById(userInfo);
        User user = this.getById(userInfo.getUserId());
        UserVo userVo = BeanUtil.copyProperties(user, UserVo.class);
        redis.set(RedisKey.getLoginUserKey(token), JSONUtil.toJsonStr(userVo), 60 * 30);
        redis.del(RedisKey.getUserInfo(userInfo.getUserId()));
        log.info("用户信息{}",userInfo);
        return R.success();
    }

    @Override
    public R bindEmail(Long userId, String email, String token) {
        UpdateWrapper<User> updateWrapper = new UpdateWrapper();
        updateWrapper.eq("user_id", userId).set("email", email);
        this.update(updateWrapper);
        User user = this.getById(userId);
        UserVo userVo = BeanUtil.copyProperties(user, UserVo.class);
        String userVoJson = JSONUtil.toJsonStr(userVo);
        redis.set(RedisKey.getLoginUserKey(token),userVoJson);
        return R.success();
    }

    @Override
    public R getSimpleUserInfoList() {
        String userInfoVosJson = redis.get(RedisKey.getUserInfos());
        List<UserInfo> userInfoList = new ArrayList<>();
        List<UserInfoVo> userInfoVos = new ArrayList<>();
        Map<String, List<UserSimpleVo>> map = new HashMap<>();
        if (StrUtil.isNotBlank(userInfoVosJson)) {
            userInfoVos = JSONUtil.toList(userInfoVosJson, UserInfoVo.class);
//            userInfoVos = userInfoToVoList(userInfoList);
        } else {
            userInfoList = userInfoMapper.selectList(null);
            userInfoVos = userInfoToVoList(userInfoList);
            redis.set(RedisKey.getUserInfos(), JSONUtil.toJsonStr(userInfoVos), 60 * 5);
        }
        map = studentClassification2(userInfoVos);

        return R.success(map);
    }

    private List<UserInfoVo> userInfoToVoList(List<UserInfo> userInfoList) {
        List<UserInfoVo> userInfoVos = new ArrayList<>();
        userInfoList.forEach(userInfo -> {
            UserInfoVo userInfoVo = getUserInfoVo(userInfo);
            userInfoVos.add(userInfoVo);
        });
        return userInfoVos;
    }

    /**
     * 封装了设置首页用户信息IndexUserVo的代码
     *
     * @param userVo
     * @return IndexUserVo
     * @author huiyuan
     */
    private IndexUserVo setIndexUserVoParams(UserVo userVo) {
        Long userId = userVo.getUserId();
        IndexUserVo indexUserVo = new IndexUserVo();
        //1.获取字符串形式用户权限名
        indexUserVo.setRoleName(getUserRightName(userVo.getUserRight()));
        //2.查询用户项目数量
        QueryWrapper<ProjectMember> projectMemberQueryWrapper = new QueryWrapper();
        projectMemberQueryWrapper.eq("user_id", userId);
        Integer projectCount = projectMemberMapper.selectCount(projectMemberQueryWrapper);
        //3.查询用户正在比赛数量
        int competitionCount = competitionSituationMapper.queryCompetitionCount(userId);
        //4.查询用户奖项数量
        QueryWrapper<Award> awardQueryWrapper = new QueryWrapper<>();
        awardQueryWrapper.eq("user_id", userId);
        Integer awardCount = awardMapper.selectCount(awardQueryWrapper);
        //todo 查询用户专利数量

        //给indexUserVo属性赋值
        indexUserVo.setProjectCount(projectCount);
        indexUserVo.setCompetitionCount(competitionCount);
        indexUserVo.setAwardCount(awardCount);
        indexUserVo.setUserId(userId);

        return indexUserVo;
    }

    private UserInfoVo getUserInfoVo(UserInfo userInfo) {
        if (userInfo == null) {
            throw new CustomException(ResponseStatusEnum.NOT_FOUND_ERROR);
        }
        UserInfoVo userInfoVo = BeanUtil.copyProperties(userInfo, UserInfoVo.class);
        //专业班级、年级、内网、头像
        userInfoVo.setMajorAndClassNumber(userInfo.getMajor() + userInfo.getClassNumber());
        //已在user_info表及其实体类增加grade字段
        userInfoVo.setGrade(getStuGrade(userInfo.getEnrollmentYear()));
        userInfoVo.setIntranetIPs(getUserIntranetIPs(userInfo.getUserId()));
        userInfoVo.setAvatar(getUserAvatar(userInfo.getUserId()));
        return userInfoVo;
    }

    /**
     * 获取用户年级, --暂时弃用
     * todo 未完善
     * @author huiyuan
     * @param enrollmentYear
     * @return true代表往届，false代表本届
     */
    private boolean currentOrPrevious(Date enrollmentYear) {
        if (enrollmentYear == null) {
            throw new CustomException(ResponseStatusEnum.SYSTEM_ERROR);
        }
        Date date = new Date();
        long l = date.getTime() - enrollmentYear.getTime();
        //计算出现在距离入学时间的天数
        l = l / 1000 / 86400;
        long year = l / 365;
        return year > 3;
    }

    private String getStuGrade(Date enrollmentYear) {
        if (enrollmentYear == null) {
            throw new CustomException(ResponseStatusEnum.SYSTEM_ERROR);
        }
        int grade = 0;
        Date date = new Date();
        long l = date.getTime() - enrollmentYear.getTime();
        //计算出现在距离入学时间的天数
        l = l / 1000 / 86400;
        long month = l / 30;
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int stuYear = getStuYear((int) month, currentYear);
        return String.valueOf(stuYear);
    }

    private int getStuYear(int month, int currentYear) {
        if (month >= 4 && month <= 12) {
            currentYear = currentYear - 1;
        }
        if (month > 12 && month <= 24) {
            currentYear = currentYear - 2;
        }
        if (month > 24 && month <= 36) {
            currentYear = currentYear - 3;
        }
        if (month > 36 && month <= 48) {
            currentYear = currentYear - 4;
        }
        return currentYear;
    }

    /**
     * 获取用户的内网IP
     *
     * @param userId
     * @return
     */
    private List<String> getUserIntranetIPs(Long userId) {
        QueryWrapper<Intranet> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        List<Intranet> intranets = intranetMapper.selectList(queryWrapper);
        List<String> intranetIPs = null;
        for (Intranet intranet : intranets) {
            intranetIPs.add(intranet.getIntranetIp());
        }
        return intranetIPs;
    }

    /**
     * 获取与用户相关的项目信息
     * @param userId
     * @return
     */
    private List<ProjectVo> getUserProjectVos(Long userId) {
        List<Project> userProjects = projectMapper.getUserProject(userId);
        if (userProjects == null || userProjects.isEmpty()) {
            return Collections.emptyList();
        }
        List<ProjectVo> projectVos = new ArrayList<>();
        for (int i = 0; i < userProjects.size(); i++) {
            Project project = userProjects.get(i);
            ProjectVo projectVo = new ProjectVo();
            //项目名称
            projectVo.setProjectName(project.getProjectName());
            //项目归属年度
            projectVo.setYear(project.getYear());
            //项目级别
            projectVo.setLevel(getProjectLevel(project.getLevel()));
            //项目状态
            projectVo.setStatus(getProjectStatus(project.getStatus()));
            //项目成员角色
            projectVo.setProjectRole(getProjectRole(project, userId));

            projectVos.add(projectVo);
        }
        return projectVos;
    }


    private String getProjectLevel(Integer level) {
        String projectLevel = "";
        switch (level) {
            case 0:
                projectLevel = "国家级";
                break;
            case 1:
                projectLevel = "省级";
                break;
            case 2:
                projectLevel = "校级";
                break;
        }
        return projectLevel;
    }

    private String getProjectStatus(Integer status) {
        String projectStatus = "";
        switch (status) {
            case 0:
                projectStatus = "正在开发";
                break;
            case 1:
                projectStatus = "已结项";
                break;
        }
        return projectStatus;
    }

    private String getProjectRole(Project project, Long userId) {
        QueryWrapper<ProjectMember> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("project_id", project.getProjectId())
                .eq("user_id", userId);
        ProjectMember projectMember = projectMemberMapper.selectOne(queryWrapper);
        Integer role = projectMember.getRole();
        String projectRole = "";
        switch (role) {
            case 0:
                projectRole = "负责人";
                break;
            case 1:
                projectRole = "普通成员";
                break;
        }
        return projectRole;
    }

    private String getUserAvatar(Long userId) {
        if (userId == null) {
            throw new CustomException(ResponseStatusEnum.NOT_FOUND_ERROR);
        }
        return this.getById(userId).getAvatar();
    }

    private List<AwardVo> getUserAwardVos(Long userId) {
        QueryWrapper<Award> awardQueryWrapper = new QueryWrapper<>();
        awardQueryWrapper.eq("user_id", userId).orderByDesc("award_time");
        List<Award> awards = awardMapper.selectList(awardQueryWrapper);
        List<AwardVo> awardVos = new ArrayList<>();
        awards.forEach(award -> {
            String competitionName = competitionMapper.selectById(award.getCompetitionId()).getCompetitionName();
            AwardVo awardVo = new AwardVo();
            awardVo.setAwardTime(award.getAwardTime());
            awardVo.setCompetitionName(competitionName);
            awardVo.setLevel(getProjectLevel(award.getAwardLevel()));
            awardVos.add(awardVo);
        });

        return awardVos;
    }
}