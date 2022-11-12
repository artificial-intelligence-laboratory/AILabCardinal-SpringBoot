package com.ailab.ailabsystem.annotation;

import com.ailab.ailabsystem.enums.UserRole;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author xiaozhi
 * @description 权限校验注解
 * @create 2022-11-2022/11/12 14:38
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthCheck {

    /**
     * 必须拥有对应角色
     */
    UserRole mustRole() default UserRole.USER;

    /**
     * 必须拥有对应权限
     */
    String mustJurisdiction() default "";
}
