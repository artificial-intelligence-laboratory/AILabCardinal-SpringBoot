package com.ailab.ailabsystem.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 发送邮箱验证码工具类
 */
@Component
public class SendEmailUtils {
    /**
     * 发送验证码
     *
     * @param email  接收邮箱
     * @param code   验证码
     * @return void
     */
    //SpringBoot 提供了应该发邮件的简单抽象，
    // 使用的是下面这个接口，这里直接注入即可使用
    @Autowired
    private JavaMailSender javaMailSender;

    //根据配置文件中自己的QQ邮箱
    private static final String from = "1713471338@qq.com";

    public static final String subject = "南方少年App邮箱绑定验证码";

    public static final String prefix = "您的南方少年App邮箱绑定验证码为:";

    public static final String suffix = "，为了您的账号安全，请勿外泄，该验证码5分钟内有效";

    /*
    @Param to 收件人
    @Param subject 主题
    @Param content
     */

    public static String getContent(String content) {
        return prefix + content + suffix;
    }

    //发送普通邮件
    public void sendSimpleMail(String to, String subject, String content) {
        //创建SimpleMailMessage对象
        SimpleMailMessage message = new SimpleMailMessage();
        //邮件发送
        message.setFrom(from);
        //邮件接收人,可以同时发给很多人
        //message.setTo(string1,string2,string3,string4,);
        message.setTo(to);
        //邮件主体
        message.setSubject(subject);
        //邮件内容
        message.setText(content);
        //通过JavaMailSender类把邮件发送出去
        javaMailSender.send(message);
        System.out.println("发送成功");
    }

}