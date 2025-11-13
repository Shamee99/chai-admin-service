package org.shamee.common.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.core.regex.ReUtil;
import org.shamee.common.annotation.Anonymous;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.*;
import java.util.regex.Pattern;

/**
 * 允许匿名访问的URL配置
 *
 * @author shamee
 * @since 2024-01-01
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class PermitAllUrlProperties implements InitializingBean {

    private static final Pattern PATTERN = Pattern.compile("\\{(.*?)}");

    private final RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Setter
    @Getter
    private List<String> urls = new ArrayList<>();

    private static final String ASTERISK = "*";

    @Override
    public void afterPropertiesSet() {
        try {
            Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping.getHandlerMethods();

            map.keySet().forEach(info -> {
                HandlerMethod handlerMethod = map.get(info);

                // 获取方法上边的注解 替代path variable 为 *
                Anonymous method = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), Anonymous.class);
                Optional.ofNullable(method).ifPresent(anonymous -> {
                    assert info.getPathPatternsCondition() != null;
                    Objects.requireNonNull(info.getPathPatternsCondition().getPatternValues())
                            .forEach(url -> urls.add(ReUtil.replaceAll(url, PATTERN, ASTERISK)));
                });

                // 获取类上边的注解, 替代path variable 为 *
                Anonymous controller = AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), Anonymous.class);
                Optional.ofNullable(controller).ifPresent(anonymous -> {
                    assert info.getPathPatternsCondition() != null;
                    Objects.requireNonNull(info.getPathPatternsCondition().getPatternValues())
                            .forEach(url -> urls.add(ReUtil.replaceAll(url, PATTERN, ASTERISK)));
                });
            });
        } catch (Exception e) {
            log.error("获取匿名访问URL失败: {}", e.getMessage(), e);
        }
    }
}