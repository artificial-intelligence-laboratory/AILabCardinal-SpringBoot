package com.ailab.ailabsystem.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class UserInfoDTO {

    private Long userInfoId;

    /**
     * 用户头像URL
     */
    private String avatar;

    /**
     * 生日
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date birthday;

    /**
     * 用户年级
     */
    private String grade;

    /**
     * 籍贯
     */
    private String nativePlace;

    /**
     * 学院
     */
    private String college;

    /**
     * 专业班级
     */
    private String majorAndClassNumber;

    /**
     * github id
     */
    private String githubId;

}
