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
import com.ailab.ailabsystem.model.vo.UserInfoVo;
import com.ailab.ailabsystem.model.vo.UserVo;
import com.ailab.ailabsystem.service.UserService;
import com.ailab.ailabsystem.util.RedisOperator;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

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
    private CompetitionSituationMapper competitionSituationMapper;

    @Resource
    private AwardMapper awardMapper;

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
        if(userLogin.getUserStatus() == UserStatus.DISABLE.getCode()){
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
     * 封装了设置首页用户信息IndexUserVo的代码
     * @param user
     * @return IndexUserVo
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
}