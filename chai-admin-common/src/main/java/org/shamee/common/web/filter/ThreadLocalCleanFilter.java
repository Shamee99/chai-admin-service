package org.shamee.common.web.filter;

import jakarta.servlet.*;
import org.shamee.common.util.log.LogMDCUtil;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 用于初始化和释放ThreadLocal的过滤器
 * @author shamee
 */
@Component
public class ThreadLocalCleanFilter implements Filter {


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        } finally {
            clearContext();
        }
    }

    private void clearContext(){
        clearMDC();
    }

    private void clearMDC() {
        LogMDCUtil.clearUserInfo();
    }

}