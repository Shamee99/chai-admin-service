package org.shamee.common.web.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.shamee.common.util.log.LogMDCUtil;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@Order(1) // 确保在其他过滤器之前执行
public class LogMDCFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("LogMDCFilter 初始化完成");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        try {
            // 设置用户信息到MDC
            LogMDCUtil.setUserInfo();

            // 记录请求开始日志（可选）
            if (log.isDebugEnabled()) {
                String requestURI = httpRequest.getRequestURI();
                String method = httpRequest.getMethod();
                String userInfo = LogMDCUtil.getUserInfoSummary();
                log.debug("请求开始: {} {} - 用户: {}", method, requestURI, userInfo);
            }

            // 继续执行过滤器链
            chain.doFilter(request, response);

        } finally {
            // 请求结束后清除MDC信息
            LogMDCUtil.clearUserInfo();
        }
    }

    @Override
    public void destroy() {
        log.info("LogMDCFilter 销毁完成");
    }
}
