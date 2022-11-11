package com.ailab.ailabsystem.service.impl;

import com.ailab.ailabsystem.mapper.UserMapper;
import com.ailab.ailabsystem.model.entity.User;
import com.ailab.ailabsystem.service.LoginService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl extends ServiceImpl<UserMapper, User> implements LoginService {
}