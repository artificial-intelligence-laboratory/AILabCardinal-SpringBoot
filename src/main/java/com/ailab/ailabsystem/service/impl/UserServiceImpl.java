package com.ailab.ailabsystem.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
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
import com.ailab.ailabsystem.model.entity.*;
import com.ailab.ailabsystem.model.vo.IndexUserVo;
import com.ailab.ailabsystem.model.vo.ProjectVo;
import com.ailab.ailabsystem.model.vo.UserInfoVo;
import com.ailab.ailabsystem.model.vo.UserVo;
import com.ailab.ailabsystem.service.UserService;
import com.ailab.ailabsystem.util.RedisOperator;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.swagger.models.auth.In;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import springfox.documentation.spring.web.json.Json;

import javax.annotation.Resource;
import java.util.*;

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
    private RedisOperator redis;

    @Override
    public R<Object> login(LoginRequest loginRequest) {
        // 将获取到的密码进行MD5加密处理(先写在这里，之后再考虑)
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
        String userJson = JSONUtil.toJsonStr(userLogin);
        // 存入redis
        redis.set(loginUserKey, userJson);
        HashMap<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("user", userVo);
        return R.success(map);
    }

    @Override
    public Map<String, List<UserInfoVo>> getUserInfoList() {
        Map<String, List<UserInfoVo>> map = null;
        // 查缓存
        String jsonMap = redis.get(RedisKey.getUserInfos());
        if (StringUtils.isNotBlank(jsonMap)) {
            map = JSONUtil.toBean(jsonMap, HashMap.class);
        } else {
            // 查询所有学生信息
            List<UserInfo> userInfos = userInfoMapper.selectList(new QueryWrapper<UserInfo>());
            map = studentClassification(userInfos);
        }
        return map;
    }

    private Map<String, List<UserInfoVo>> studentClassification(List<UserInfo> userInfos) {
        // 因为只有两个，分别是本届和往届，所以直接实例化为2
        HashMap<String, List<UserInfoVo>> map = new HashMap<>(2);
        List<UserInfoVo> currentStudents = new ArrayList<>();
        List<UserInfoVo> previousStudents = new ArrayList<>();
        userInfos.forEach(userInfo -> {
            UserInfoVo userInfoVo = BeanUtil.copyProperties(userInfo, UserInfoVo.class);
            // 获取年级
            String grade = userInfo.getClassNumber().toString().substring(0, 2);
            userInfoVo.setGrade(CommonConstant.GRADE_PRE + grade + "级");
            // 获取当前时间
            Date today = new Date();
        });
        map.put(CommonConstant.CURRENT_STUDENTS, currentStudents);
        map.put(CommonConstant.PREVIOUS_STUDENTS, previousStudents);
        // 放入缓存
        redis.set(RedisKey.getUserInfos(), JSONUtil.toJsonStr(map));
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
        User user = JSONUtil.toBean(userJson, User.class);
        Long userId = user.getUserId();

        //注意上面查的是登录用户的信息（User），而下面查的是用户的首页信息（IndexUserInfo）
        //先查redis的首页用户
        String indexUserKey = RedisKey.getIndexUser(userId);
        String resultUserJson = redis.get(indexUserKey);
        if (StrUtil.isNotBlank(resultUserJson)) {
            return R.success(JSONUtil.toBean(resultUserJson, IndexUserVo.class));
        }
        IndexUserVo indexUserVo = setIndexUserVoParams(user);

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
        }
        //存在则直接把JSON转化为实体类
        userInfoVo = JSONUtil.toBean(userInfoJson, UserInfoVo.class);
        //获取与用户相关的项目
        String userProjectKey = RedisKey.getUserProject(userId);
        String userProjectJson = redis.get(userProjectKey);
        List<ProjectVo> userProjectVos = new ArrayList<>();
        if (StrUtil.isBlank(userProjectJson)) {
            userProjectVos = getUserProjectVos(userInfo.getUserId());
            String userProjectJsonStr = JSONUtil.toJsonStr(userProjectVos);
            redis.set(userProjectKey, userProjectJsonStr, 60 * 30);
        }
        userProjectVos = JSONUtil.toList(userProjectJson, ProjectVo.class);
        Map<String, Object> map = new HashMap<>();
        map.put("userInfo", userInfoVo);
        map.put("userProjectVos", userProjectVos);

        return R.success(map);
    }

    /**
     * 封装了设置首页用户信息IndexUserVo的代码
     *
     * @param user
     * @return IndexUserVo
     * @author huiyuan
     */
    private IndexUserVo setIndexUserVoParams(User user) {
        Long userId = user.getUserId();
        IndexUserVo indexUserVo = new IndexUserVo();
        //1.获取字符串形式用户权限名
        indexUserVo.setRoleName(getUserRightName(user.getUserRight()));
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
        UserInfoVo userInfoVo = BeanUtil.copyProperties(userInfo, UserInfoVo.class);
        //专业班级、年级、内网
        userInfoVo.setMajorAndClassNumber(userInfo.getMajor() + userInfo.getClassNumber());
        userInfoVo.setGrade(getUserGrade(userInfo.getEnrollmentYear()));
        userInfoVo.setIntranetIPs(getUserIntranetIPs(userInfo.getUserId()));

        return userInfoVo;
    }

    /**
     * 获取用户年级
     * todo 未完善
     *
     * @param enrollmentYear
     * @return
     */
    private String getUserGrade(Date enrollmentYear) {
        if (enrollmentYear == null) {
            throw new CustomException(ResponseStatusEnum.SYSTEM_ERROR);
        }
        String grade = "";
        Date date = new Date();
        long l = date.getTime() - enrollmentYear.getTime();
        //计算出现在距离入学时间的天数
        l = l / 1000 / 86400;
        long year = l / 365;
        switch ((int) year) {
            case 0:
                grade = "大一";
                break;
            case 1:
                grade = "大二";
                break;
            case 2:
                grade = "大三";
                break;
            case 3:
                grade = "大四";
                break;
            default:
                grade = "往届";
                break;
        }
        return grade;
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
     *
     * @param userId
     * @return
     */
    private List<ProjectVo> getUserProjectVos(Long userId) {
        List<Project> userProjects = projectMapper.getUserProject(userId);
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
}