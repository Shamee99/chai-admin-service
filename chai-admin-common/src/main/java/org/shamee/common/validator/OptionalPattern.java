package org.shamee.common.validator;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 可选校验注解，当字段如果为空时候，不进行校验。
 * 如果不为空，则校验
 *
 * @author shamee
 * @date 2025-07-15
 *
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = OptionalPatternValidator.class)
public @interface OptionalPattern {

    Class<? extends Annotation>[] checks() default {};

    String pattern() default "";

    String message() default "格式不正确";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
