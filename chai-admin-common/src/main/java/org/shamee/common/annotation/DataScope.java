package org.shamee.common.annotation;

import java.lang.annotation.*;

/**
 * 数据权限过滤注解
 * 用于标记需要进行数据权限过滤的方法
 *
 * @author shamee
 * @since 2024-01-01
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataScope {

    /**
     * 部门表别名
     * 默认为 "d"
     */
    String deptAlias() default "d";

    /**
     * 用户表别名
     * 默认为 "u"
     */
    String userAlias() default "u";

    /**
     * 部门ID字段名
     * 默认为 "dept_id"
     */
    String deptColumn() default "dept_id";

    /**
     * 用户ID字段名
     * 默认为 "user_id"
     */
    String userColumn() default "user_id";
}
