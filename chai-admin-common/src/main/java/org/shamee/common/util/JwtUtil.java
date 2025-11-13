package org.shamee.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.core.text.StrUtil;
import org.shamee.common.constant.CommonConstant;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * JWT工具类
 *
 * @author shamee
 * @since 2024-01-01
 */
@Slf4j
@Component
public class JwtUtil {

    /**
     * 密钥
     */
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(CommonConstant.Jwt.SECRET.getBytes());

    /**
     * 生成访问令牌
     *
     * @param userId 用户ID
     * @param username 用户名
     * @param authorities 权限列表
     * @return JWT令牌
     */
    public static String generateAccessToken(String userId, String username, String authorities) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + CommonConstant.Jwt.EXPIRATION);

        return Jwts.builder()
                .subject(username)
                .claim(CommonConstant.Jwt.CLAIM_USER_ID, userId)
                .claim(CommonConstant.Jwt.CLAIM_USERNAME, username)
                .claim(CommonConstant.Jwt.CLAIM_AUTHORITIES, authorities)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(SECRET_KEY)
                .compact();
    }

    /**
     * 生成刷新令牌
     *
     * @param userId 用户ID
     * @param username 用户名
     * @return 刷新令牌
     */
    public static String generateRefreshToken(String userId, String username) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + CommonConstant.Jwt.REFRESH_EXPIRATION);

        return Jwts.builder()
                .subject(username)
                .claim(CommonConstant.Jwt.CLAIM_USER_ID, userId)
                .claim(CommonConstant.Jwt.CLAIM_USERNAME, username)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(SECRET_KEY)
                .compact();
    }

    /**
     * 解析JWT令牌
     *
     * @param token JWT令牌
     * @return Claims
     */
    public static Claims parseToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(SECRET_KEY)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            log.error("解析JWT令牌失败: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 验证JWT令牌
     *
     * @param token JWT令牌
     * @return 是否有效
     */
    public static boolean validateToken(String token) {
        try {
            if (StrUtil.isBlank(token)) {
                return false;
            }
            
            Claims claims = parseToken(token);
            if (claims == null) {
                return false;
            }
            
            // 检查是否过期
            Date expiration = claims.getExpiration();
            return expiration != null && expiration.after(new Date());
        } catch (Exception e) {
            log.error("验证JWT令牌失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 从令牌中获取用户ID
     *
     * @param token JWT令牌
     * @return 用户ID
     */
    public static String getUserId(String token) {
        Claims claims = parseToken(token);
        if (claims != null) {
            Object userId = claims.get(CommonConstant.Jwt.CLAIM_USER_ID);
            return String.valueOf(userId);
        }
        return null;
    }

    /**
     * 从令牌中获取用户名
     *
     * @param token JWT令牌
     * @return 用户名
     */
    public static String getUsername(String token) {
        Claims claims = parseToken(token);
        return claims != null ? claims.get(CommonConstant.Jwt.CLAIM_USERNAME, String.class) : null;
    }

    /**
     * 从令牌中获取权限
     *
     * @param token JWT令牌
     * @return 权限字符串
     */
    public static String getAuthorities(String token) {
        Claims claims = parseToken(token);
        return claims != null ? claims.get(CommonConstant.Jwt.CLAIM_AUTHORITIES, String.class) : null;
    }

    /**
     * 获取令牌过期时间
     *
     * @param token JWT令牌
     * @return 过期时间
     */
    public static Date getExpiration(String token) {
        Claims claims = parseToken(token);
        return claims != null ? claims.getExpiration() : null;
    }

    /**
     * 检查令牌是否即将过期（30分钟内）
     *
     * @param token JWT令牌
     * @return 是否即将过期
     */
    public static boolean isTokenExpiringSoon(String token) {
        Date expiration = getExpiration(token);
        if (expiration == null) {
            return true;
        }
        
        long timeUntilExpiration = expiration.getTime() - System.currentTimeMillis();
        return timeUntilExpiration < 30 * 60 * 1000; // 30分钟
    }

    /**
     * 从请求头中提取令牌
     *
     * @param authHeader Authorization请求头
     * @return JWT令牌
     */
    public static String extractToken(String authHeader) {
        if (StrUtil.isNotBlank(authHeader) && authHeader.startsWith(CommonConstant.Jwt.TOKEN_PREFIX)) {
            return authHeader.substring(CommonConstant.Jwt.TOKEN_PREFIX.length());
        }
        return null;
    }
}