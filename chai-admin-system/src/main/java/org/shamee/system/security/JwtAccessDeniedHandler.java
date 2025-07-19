package org.shamee.system.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.json.JSONUtil;
import org.shamee.common.dto.resp.R;
import org.shamee.common.dto.resp.ResultCode;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * JWT访问拒绝处理器
 * 处理权限不足的请求
 *
 * @author shamee
 * @since 2024-01-01
 */
@Slf4j
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        
        log.warn("权限不足的请求: {}, 异常: {}", request.getRequestURI(), accessDeniedException.getMessage());
        
        // 设置响应头
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        
        // 构建响应结果
        R<Void> r = R.error(ResultCode.FORBIDDEN, "权限不足");
        
        // 写入响应
        response.getWriter().write(JSONUtil.toJsonStr(r));
        response.getWriter().flush();
    }
}