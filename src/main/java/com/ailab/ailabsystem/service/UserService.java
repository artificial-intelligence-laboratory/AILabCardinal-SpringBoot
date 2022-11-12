package com.ailab.ailabsystem.service;

import com.ailab.ailabsystem.common.R;
import com.ailab.ailabsystem.model.dto.LoginRequest;
import com.ailab.ailabsystem.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

public interface UserService extends IService<User> {

    R<Object> login(LoginRequest loginRequest);
}
