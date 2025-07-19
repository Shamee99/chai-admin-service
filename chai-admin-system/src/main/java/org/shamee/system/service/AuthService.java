package org.shamee.system.service;

import org.shamee.system.dto.req.auth.LoginRequest;
import org.shamee.system.dto.resp.auth.LoginResp;

/**
 * 认证服务接口
 *
 * @author shamee
 * @since 2024-01-01
 */
public interface AuthService {

    /**
     * 用户登录
     *
     * @param loginRequest 登录请求
     * @return 登录结果
     */
    LoginResp login(LoginRequest loginRequest);

    /**
     * 用户登出
     *
     * @param token JWT令牌
     * @return 是否成功
     */
    boolean logout(String token);

    /**
     * 刷新令牌
     *
     * @param refreshToken 刷新令牌
     * @return 新的访问令牌
     */
    LoginResp refreshToken(String refreshToken);

    /**
     * 获取当前用户信息
     *
     * @return 用户信息
     */
    Object getCurrentUser();

    /**
     * 验证令牌
     *
     * @param token JWT令牌
     * @return 是否有效
     */
    boolean validateToken(String token);
}