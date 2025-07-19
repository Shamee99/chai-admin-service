package org.shamee.system.annotation;

import java.lang.annotation.*;


/**
 * 登录日志注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LoginLog {
    /**
     * 登录类型
     */
    String type() default "login";

    /**
     * 登录描述
     */
    String description() default "";
}