package org.shamee.system.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.core.text.StrUtil;
import org.shamee.common.constant.CommonConstant;
import org.shamee.common.dto.resp.R;
import org.shamee.common.util.JwtUtil;
import org.shamee.system.annotation.LoginLog;
import org.shamee.system.dto.req.auth.LoginRequest;
import org.shamee.system.dto.resp.auth.LoginResp;
import org.shamee.system.service.AuthService;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 *
 * @author shamee
 * @since 2024-01-01
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 用户登录
     *
     * @param loginRequest 登录请求
     * @return 登录结果
     */
    @LoginLog(description = "用户登录")
    @PostMapping("/login")
    public R<LoginResp> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            LoginResp result = authService.login(loginRequest);
            return R.success(result);
        } catch (Exception e) {
            log.error("登录失败: {}", e.getMessage(), e);
            return R.error(e.getMessage());
        }
    }

    /**
     * 用户登出
     *
     * @param request HTTP请求
     * @return 操作结果
     */
    @PostMapping("/logout")
    public R<Void> logout(HttpServletRequest request) {
        try {
            String authHeader = request.getHeader(CommonConstant.Jwt.TOKEN_HEADER);
            String token = JwtUtil.extractToken(authHeader);
            
            if (StrUtil.isBlank(token)) {
                return R.error("令牌不能为空");
            }
            
            boolean success = authService.logout(token);
            if (success) {
                return R.success();
            } else {
                return R.error("登出失败");
            }
        } catch (Exception e) {
            log.error("登出失败: {}", e.getMessage(), e);
            return R.error("登出失败: " + e.getMessage());
        }
    }

    /**
     * 刷新令牌
     *
     * @param refreshToken 刷新令牌
     * @return 新的访问令牌
     */
    @PostMapping("/refresh")
    public R<LoginResp> refreshToken(@RequestParam String refreshToken) {
        try {
            if (StrUtil.isBlank(refreshToken)) {
                return R.error("刷新令牌不能为空");
            }
            
            LoginResp result = authService.refreshToken(refreshToken);
            return R.success(result);
        } catch (Exception e) {
            log.error("刷新令牌失败: {}", e.getMessage(), e);
            return R.error(e.getMessage());
        }
    }

    /**
     * 获取当前用户信息
     *
     * @return 用户信息
     */
    @GetMapping("/me")
    public R<Object> getCurrentUser() {
        try {
            Object user = authService.getCurrentUser();
            if (user != null) {
                return R.success(user);
            } else {
                return R.error("未找到当前用户信息");
            }
        } catch (Exception e) {
            log.error("获取当前用户信息失败: {}", e.getMessage(), e);
            return R.error("获取用户信息失败: " + e.getMessage());
        }
    }

    /**
     * 验证令牌
     *
     * @param request HTTP请求
     * @return 验证结果
     */
    @GetMapping("/validate")
    public R<Boolean> validateToken(HttpServletRequest request) {
        try {
            String authHeader = request.getHeader(CommonConstant.Jwt.TOKEN_HEADER);
            String token = JwtUtil.extractToken(authHeader);
            
            if (StrUtil.isBlank(token)) {
                return R.success(false);
            }
            
            boolean valid = authService.validateToken(token);
            return R.success(valid);
        } catch (Exception e) {
            log.error("验证令牌失败: {}", e.getMessage(), e);
            return R.success(false);
        }
    }

    /**
     * 健康检查
     *
     * @return 健康状态
     */
    @GetMapping("/health")
    public R<String> health() {
        return R.success("Auth service is running");
    }
}