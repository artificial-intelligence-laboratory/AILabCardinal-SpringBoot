package com.ailab.ailabsystem.enums;

/**
 * @author xiaozhi
 * @description 结果状态码和信息封装
 * @create 2022-10-2022/10/21 12:01
 */
public enum ResponseStatusEnum {
    SUCCESS(200, true, "操作成功！"),
    FAILED(500, false, "操作失败！"),
    PARAMS_ERROR(40000, false,"请求参数错误"),
    NOT_LOGIN_ERROR(40100,false, "未登录"),
    NO_AUTH_ERROR(40101, false, "无权限"),
    PHONE_ERROR(400102, false, "手机号格式错误"),
    EMAIL_ERROR(400103, false, "邮箱格式错误"),
    CODE_ERROR(400104, false, "验证码错误"),
    STUDENT_NUMBER_OR_PASSWORD_NULL(400105, false, "学号或密码不可为空"),
    SESSION_EXPIRE(401, false, "会话已过期，请重新登录"),
    USERNAME_PASSWORD_ERROR(401, false, "账号或密码不正确，请求重新输入"),
    NOT_FOUND_ERROR(40400, false, "请求数据不存在"),
    FORBIDDEN_ERROR(401, false, "该账号已被禁用，请联系管理员"),
    EXISTS_ERROR(402, false, "该账号已登录"),
    SYSTEM_ERROR(50000, false, "系统内部异常"),
    OPERATION_ERROR(50001, false, "操作失败"),
    ENROLLMENTYEAR_ERROR(50002, false, "入学年份不可小于2010年且不可大于今年"),
    SGININ_ERROR(411, false, "您已签到或当前非签到时间"),
    EXPORE_EXCEL_ERROR(412, false, "文件下载失败，请重试"),
    NUM_ERROR(413, false, "最大人数已经超过存在人数");

    // 响应业务状态
    private Integer status;
    // 调用是否成功
    private Boolean success;
    // 响应消息，可以为成功或者失败的消息
    private String msg;

    ResponseStatusEnum(Integer status, Boolean success, String msg) {
        this.status = status;
        this.success = success;
        this.msg = msg;
    }

    public Integer status() {
        return status;
    }
    public Boolean success() {
        return success;
    }
    public String msg() {
        return msg;
    }
}
