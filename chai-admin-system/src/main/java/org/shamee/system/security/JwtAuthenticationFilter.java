package org.shamee.system.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.core.text.StrUtil;
import org.shamee.common.config.PermitAllUrlProperties;
import org.shamee.common.constant.CommonConstant;
import org.shamee.common.util.JwtUtil;
import org.shamee.common.util.RedisUtil;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * JWT认证过滤器
 *
 * @author shamee
 * @since 2024-01-01
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final RedisUtil redisUtil;
    private final PermitAllUrlProperties permitAllUrlProperties;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        
        // 获取Authorization头
        String authHeader = request.getHeader(CommonConstant.Jwt.TOKEN_HEADER);
        
        // 提取JWT令牌
        String token = JwtUtil.extractToken(authHeader);
        
        if (StrUtil.isBlank(token)) {
            filterChain.doFilter(request, response);
            return;
        }
        
        try {
            // 验证令牌
            if (!JwtUtil.validateToken(token)) {
                log.warn("JWT令牌验证失败: {}", token.substring(0, Math.min(token.length(), 20)) + "...");
                filterChain.doFilter(request, response);
                return;
            }
            
            // 解析令牌
            Claims claims = JwtUtil.parseToken(token);
            if (claims == null) {
                log.warn("JWT令牌解析失败");
                filterChain.doFilter(request, response);
                return;
            }
            
            String username = JwtUtil.getUsername(token);
            String userId = JwtUtil.getUserId(token);
            
            if (StrUtil.isBlank(username) || StrUtil.isBlank(userId)) {
                log.warn("JWT令牌中缺少用户信息");
                filterChain.doFilter(request, response);
                return;
            }
            
            // 检查Redis中的令牌是否存在（用于分布式环境下的令牌管理）
            String redisKey = CommonConstant.RedisKey.USER_TOKEN + userId;
            String cachedToken = redisUtil.get(redisKey, String.class);
            
            if (StrUtil.isBlank(cachedToken) || !token.equals(cachedToken)) {
                log.warn("Redis中不存在对应的令牌或令牌不匹配, userId: {}", userId);
                filterChain.doFilter(request, response);
                return;
            }
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // 如果当前没有认证信息，则设置认证
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                // 获取权限信息
                String authoritiesStr = JwtUtil.getAuthorities(token);
                List<SimpleGrantedAuthority> authorities = Arrays.stream(
                        StrUtil.isNotBlank(authoritiesStr) ? authoritiesStr.split(",") : new String[0]
                ).map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
                
                // 创建认证令牌
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, authorities
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                
                // 设置到安全上下文
                SecurityContextHolder.getContext().setAuthentication(authToken);
                
                log.debug("JWT认证成功, 用户: {}, 权限数量: {}", username, authorities.size());
            }
            
        } catch (Exception e) {
            log.error("JWT认证过程中发生异常: {}", e.getMessage(), e);
        }
        
        filterChain.doFilter(request, response);
    }

    @Override
    public void afterPropertiesSet() throws ServletException {
        super.afterPropertiesSet();
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // 对于登录、注册等公开接口，不进行JWT认证
        String path = request.getRequestURI();
        return path.startsWith("/api/auth/") || 
               path.startsWith("/api/public/") ||
               path.equals("/favicon.ico") ||
               path.startsWith("/actuator/") ||
               permitAllUrlProperties.getUrls().contains(path);
    }
}