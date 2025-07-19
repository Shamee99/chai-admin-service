package org.shamee.common.annotation;

import java.lang.annotation.*;


/**
 * 允许匿名访问注解
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Anonymous {
}
