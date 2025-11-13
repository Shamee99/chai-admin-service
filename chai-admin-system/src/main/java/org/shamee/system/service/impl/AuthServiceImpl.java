package org.shamee.system.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.http.server.servlet.ServletUtil;
import org.shamee.common.constant.CommonConstant;
import org.shamee.common.dto.resp.UserPrincipal;
import org.shamee.common.util.JwtUtil;
import org.shamee.common.util.RedisUtil;
import org.shamee.system.entity.SysUser;
import org.shamee.system.dto.req.auth.LoginRequest;
import org.shamee.system.dto.resp.auth.LoginResp;
import org.shamee.system.service.AuthService;
import org.shamee.system.service.SysUserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * 认证服务实现
 *
 * @author shamee
 * @since 2024-01-01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final SysUserService sysUserService;
    private final RedisUtil redisUtil;

    @Override
    public LoginResp login(LoginRequest loginRequest) {
        log.info("用户登录: {}", loginRequest.getUsername());
        
        try {
            // 创建认证令牌
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(), loginRequest.getPassword()
            );
            
            // 进行认证
            Authentication authentication = authenticationManager.authenticate(authToken);
            
            // 获取用户信息
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            
            // 生成JWT令牌
            String accessToken = JwtUtil.generateAccessToken(
                    userPrincipal.getUserId(),
                    userPrincipal.getUsername(),
                    userPrincipal.getAuthoritiesString()
            );
            
            String refreshToken = JwtUtil.generateRefreshToken(
                    userPrincipal.getUserId(),
                    userPrincipal.getUsername()
            );
            
            // 将令牌存储到Redis（用于分布式环境下的令牌管理）
            String tokenKey = CommonConstant.RedisKey.USER_TOKEN + userPrincipal.getUserId();
            redisUtil.set(tokenKey, accessToken, CommonConstant.Jwt.EXPIRATION, TimeUnit.MILLISECONDS);
            
            // 存储用户信息到Redis
            String userInfoKey = CommonConstant.RedisKey.USER_INFO + userPrincipal.getUserId();
            redisUtil.set(userInfoKey, userPrincipal, CommonConstant.Jwt.EXPIRATION, TimeUnit.MILLISECONDS);
            
            // 更新用户登录信息
            updateLoginInfo(userPrincipal.getUserId());
            
            // 构建登录结果
            LoginResp.UserInfo userInfo = LoginResp.UserInfo.builder()
                    .userId(userPrincipal.getUserId())
                    .username(userPrincipal.getUsername())
                    .realName(userPrincipal.getRealName())
                    .email(userPrincipal.getEmail())
                    .phone(userPrincipal.getPhone())
                    .deptId(userPrincipal.getDeptId())
                    .deptName(userPrincipal.getDeptName())
                    .roles(userPrincipal.getRoles())
                    .permissions(userPrincipal.getPermissions())
                    .lastLoginTime(LocalDateTime.now())
                    .lastLoginIp(getClientIp())
                    .build();
            
            LoginResp result = LoginResp.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .tokenType("Bearer")
                    .expiresIn(CommonConstant.Jwt.EXPIRATION / 1000)
                    .userInfo(userInfo)
                    .build();
            
            log.info("用户登录成功: {}", loginRequest.getUsername());
            return result;
            
        } catch (BadCredentialsException e) {
            log.warn("用户登录失败，用户名或密码错误: {}", loginRequest.getUsername());
            throw new RuntimeException("用户名或密码错误");
        } catch (Exception e) {
            log.error("用户登录失败: {}, 错误: {}", loginRequest.getUsername(), e.getMessage(), e);
            throw new RuntimeException("登录失败: " + e.getMessage());
        }
    }

    @Override
    public boolean logout(String token) {
        try {
            if (StrUtil.isBlank(token)) {
                return false;
            }
            
            // 解析令牌获取用户ID
            String userId = JwtUtil.getUserId(token);
            if (userId == null) {
                return false;
            }
            
            // 从Redis中删除令牌和用户信息
            String tokenKey = CommonConstant.RedisKey.USER_TOKEN + userId;
            String userInfoKey = CommonConstant.RedisKey.USER_INFO + userId;
            
            redisUtil.delete(tokenKey);
            redisUtil.delete(userInfoKey);
            
            // 清除安全上下文
            SecurityContextHolder.clearContext();
            
            log.info("用户登出成功, userId: {}", userId);
            return true;
            
        } catch (Exception e) {
            log.error("用户登出失败: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public LoginResp refreshToken(String refreshToken) {
        try {
            // 验证刷新令牌
            if (!JwtUtil.validateToken(refreshToken)) {
                throw new RuntimeException("刷新令牌无效");
            }
            
            // 获取用户信息
            String userId = JwtUtil.getUserId(refreshToken);
            String username = JwtUtil.getUsername(refreshToken);
            
            if (userId == null || StrUtil.isBlank(username)) {
                throw new RuntimeException("刷新令牌中缺少用户信息");
            }
            
            // 获取用户权限
            SysUser user = sysUserService.getUserById(userId);
            if (user == null) {
                throw new RuntimeException("用户不存在");
            }
            
            // 获取用户权限
            var permissions = sysUserService.getUserPermissions(userId);
            String authoritiesStr = String.join(",", permissions);
            
            // 生成新的访问令牌
            String newAccessToken = JwtUtil.generateAccessToken(userId, username, authoritiesStr);
            String newRefreshToken = JwtUtil.generateRefreshToken(userId, username);
            
            // 更新Redis中的令牌
            String tokenKey = CommonConstant.RedisKey.USER_TOKEN + userId;
            redisUtil.set(tokenKey, newAccessToken, CommonConstant.Jwt.EXPIRATION, TimeUnit.MILLISECONDS);
            
            return LoginResp.builder()
                    .accessToken(newAccessToken)
                    .refreshToken(newRefreshToken)
                    .tokenType("Bearer")
                    .expiresIn(CommonConstant.Jwt.EXPIRATION / 1000)
                    .build();
            
        } catch (Exception e) {
            log.error("刷新令牌失败: {}", e.getMessage(), e);
            throw new RuntimeException("刷新令牌失败: " + e.getMessage());
        }
    }

    @Override
    public Object getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getPrincipal();
        }
        return null;
    }

    @Override
    public boolean validateToken(String token) {
        return JwtUtil.validateToken(token);
    }

    /**
     * 更新用户登录信息
     *
     * @param userId 用户ID
     */
    private void updateLoginInfo(String userId) {
        try {
            String clientIp = getClientIp();
            sysUserService.updateLoginInfo(userId,  clientIp);
        } catch (Exception e) {
            log.error("更新用户登录信息失败, userId: {}, error: {}", userId, e.getMessage());
        }
    }

    /**
     * 获取客户端IP
     *
     * @return 客户端IP
     */
    private String getClientIp() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                return ServletUtil.getClientIP(request);
            }
        } catch (Exception e) {
            log.warn("获取客户端IP失败: {}", e.getMessage());
        }
        return "unknown";
    }
}