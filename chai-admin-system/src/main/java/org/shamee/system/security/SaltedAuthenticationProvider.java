package org.shamee.system.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * 加盐认证提供者
 *
 * @author shamee
 * @since 2024-01-01
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SaltedAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsServiceImpl userDetailsService;
    private final SaltedPasswordEncoder saltedPasswordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        log.debug("开始认证用户: {}", username);

        try {
            // 加载用户详情
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            
            // 验证密码
            boolean passwordMatches = saltedPasswordEncoder.matches(
                password, 
                username, 
                userDetails.getPassword()
            );
            
            if (!passwordMatches) {
                log.warn("用户密码验证失败: {}", username);
                throw new BadCredentialsException("用户名或密码错误");
            }

            log.debug("用户认证成功: {}", username);
            
            // 创建认证成功的Authentication对象
            return new UsernamePasswordAuthenticationToken(
                userDetails,
                null, // 清除密码
                userDetails.getAuthorities()
            );
            
        } catch (AuthenticationException e) {
            log.error("用户认证失败: {}, 原因: {}", username, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("用户认证过程中发生异常: {}", username, e);
            throw new BadCredentialsException("认证过程中发生异常", e);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}