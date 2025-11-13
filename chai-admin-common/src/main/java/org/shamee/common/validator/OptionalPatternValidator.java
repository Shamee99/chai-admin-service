package org.shamee.common.validator;

import jakarta.validation.*;
import jakarta.validation.constraints.Pattern;

import java.lang.annotation.Annotation;
import java.util.Arrays;


/**
 * 自定义校验器
 * @author shamee
 * @date 2025-07-15
 */
public class OptionalPatternValidator implements ConstraintValidator<OptionalPattern, String> {

    private Validator validator;
    private Class<? extends Annotation>[] checks;
    private String pattern; // 存储注解上配置的pattern

    @Override
    public void initialize(OptionalPattern constraintAnnotation) {
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        this.checks = constraintAnnotation.checks();
        this.pattern = constraintAnnotation.pattern(); // 初始化pattern
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true;
        }

        // 优先使用直接配置的pattern
        if (!pattern.isEmpty()) {
            return value.matches(pattern);
        }

        // 否则使用checks指定的注解校验
        if (checks.length > 0) {
            return Arrays.stream(checks)
                    .allMatch(anno -> validateWithAnnotation(value, anno, context));
        }

        return true;
    }

    private boolean validateWithAnnotation(
            String value,
            Class<? extends Annotation> annoClass,
            ConstraintValidatorContext context
    ) {
        if (annoClass == Pattern.class) {
            // 动态创建@Pattern注解实例
            Pattern patternAnnotation = new Pattern() {
                @Override
                public String regexp() {
                    // 关键修改：返回外部类存储的pattern
                    return pattern;
                }

                @Override
                public Flag[] flags() {
                    return new Flag[0];
                }

                @Override
                public String message() {
                    return "{javax.validation.constraints.Pattern.message}";
                }

                @Override
                public Class<?>[] groups() {
                    return new Class[0];
                }

                @Override
                public Class<? extends Payload>[] payload() {
                    return new Class[0];
                }

                @Override
                public Class<? extends Annotation> annotationType() {
                    return Pattern.class;
                }
            };

            return validator.validateValue(String.class, value, patternAnnotation).isEmpty();
        }
        // 其他注解类型的处理...
        return true;
    }
}
