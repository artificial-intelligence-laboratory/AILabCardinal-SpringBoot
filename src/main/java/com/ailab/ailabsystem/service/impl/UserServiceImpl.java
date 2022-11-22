package com.ailab.ailabsystem.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.ailab.ailabsystem.common.CommonConstant;
import com.ailab.ailabsystem.common.R;
import com.ailab.ailabsystem.common.RedisKey;
import com.ailab.ailabsystem.enums.ResponseStatusEnum;
import com.ailab.ailabsystem.enums.UserStatus;
import com.ailab.ailabsystem.exception.CustomException;
import com.ailab.ailabsystem.mapper.UserInfoMapper;
import com.ailab.ailabsystem.mapper.UserMapper;
import com.ailab.ailabsystem.model.dto.LoginRequest;
import com.ailab.ailabsystem.model.entity.User;
import com.ailab.ailabsystem.model.entity.UserInfo;
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
    private RedisOperator redis;

    @Override
    public R<Object> login(LoginRequest loginRequest) {
        // 将获取到的密码进行MD5加密处理(先写在这里，之后再考虑)
        String password = loginRequest.getPassword();
        String studentNumber = loginRequest.getStudentNumber();
//        password = DigestUtils.md5DigestAsHex(password.getBytes());
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
            // 获取学生毕业时间
            Date graduationTime = userInfo.getGraduationTime();
            // 毕业时间在当前时间的后面为本届，其余为往届
            if (graduationTime.after(today)) {
                // 本届学生
                currentStudents.add(userInfoVo);
            } else {
                previousStudents.add(userInfoVo);
            }
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
}