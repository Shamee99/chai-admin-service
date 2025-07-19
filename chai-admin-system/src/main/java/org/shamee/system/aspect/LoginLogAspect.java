package org.shamee.system.aspect;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.dromara.hutool.http.server.servlet.ServletUtil;
import org.shamee.system.annotation.LoginLog;
import org.shamee.system.entity.SysLoginLog;
import org.shamee.system.dto.resp.auth.LoginResp;
import org.shamee.system.service.SysLoginLogService;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;

/**
 * 登录日志切面
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LoginLogAspect {


    private final SysLoginLogService sysLoginLogService;

    /**
     * 配置切入点
     */
    @Pointcut("@annotation(org.shamee.system.annotation.LoginLog)")
    public void logPointCut() {
    }

    /**
     * 处理完请求后执行
     */
    @AfterReturning(pointcut = "logPointCut()", returning = "result")
    public void doAfterReturning(JoinPoint joinPoint, Object result) {
        try {
            // 获取请求属性
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes == null) {
                return;
            }

            HttpServletRequest request = attributes.getRequest();

            // 获取注解
            LoginLog logLogin = getAnnotationLog(joinPoint);
            if (logLogin == null) {
                return;
            }

            // 获取IP地址
            String ip = ServletUtil.getClientIP(request);

            // 这里可以获取登录用户信息，根据你的实际需求实现
            // 例如从result中获取用户信息，或者从SecurityContext中获取

            log.info("登录日志: 类型={}, IP={}, 时间={}, 描述={}",
                    logLogin.type(),
                    ip,
                    LocalDateTime.now(),
                    logLogin.description());

            this.insertLoginLog(ip, result);
        } catch (Exception e) {
            log.error("记录登录日志异常: {}", e.getMessage(), e);
        }
    }


    /**
     * 获取注解
     */
    private LoginLog getAnnotationLog(JoinPoint joinPoint) {
        return ((org.aspectj.lang.reflect.MethodSignature) joinPoint.getSignature())
                .getMethod().getAnnotation(LoginLog.class);
    }


    /**
     * 登录信息入库
     * @param ip  客户端IP
     * @param result  登录返回结果
     */
    private void insertLoginLog(String ip, Object result) {
        SysLoginLog.SysLoginLogBuilder loginLogBuilder = SysLoginLog.builder().ipaddr(ip);

        if (result instanceof LoginResp loginResult) {
            loginLogBuilder.username(loginResult.getUserInfo().getUsername());
            loginLogBuilder.loginResult(loginResult);
        }

        sysLoginLogService.save(loginLogBuilder.build());
    }
}
