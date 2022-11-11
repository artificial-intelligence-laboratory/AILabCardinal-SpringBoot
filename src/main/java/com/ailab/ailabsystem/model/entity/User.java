package com.ailab.ailabsystem.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@TableName(value = "user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    private int userId;
    private String userName;
    private String password;
    private String email;
    private String avatar;
    private int userRight;
    private LocalDateTime lastOnlineTime;
    private String lastOnlineIp;
    private String lastOnlineIpAddress;
    private int userStatus;
}
