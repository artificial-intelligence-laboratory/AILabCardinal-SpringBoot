package com.ailab.ailabsystem.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.ailab.ailabsystem.common.R;
import com.ailab.ailabsystem.common.RedisKey;
import com.ailab.ailabsystem.enums.ResponseStatusEnum;
import com.ailab.ailabsystem.enums.UserStatus;
import com.ailab.ailabsystem.exception.CustomException;
import com.ailab.ailabsystem.mapper.UserMapper;
import com.ailab.ailabsystem.model.dto.LoginRequest;
import com.ailab.ailabsystem.model.entity.User;
import com.ailab.ailabsystem.model.vo.UserVo;
import com.ailab.ailabsystem.service.UserService;
import com.ailab.ailabsystem.util.RedisOperator;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.UUID;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;

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
}