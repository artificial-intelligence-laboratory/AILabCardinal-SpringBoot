package com.ailab.ailabsystem.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@TableName(value = "user_info")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {
    private int userInfoId;
    private int userid;
    private String realName;
    private String birthday;
    private LocalDateTime enrollmentYear;
    private String nativePlace;
    private String joinAilabTime;
    private String developmentDirection;
    private String college;
    private String major;
    private String githubId;
    private String weiboId;
    private String studentNumber;
    private String dormitoryNumber;
}