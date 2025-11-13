package org.shamee.system.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shamee.common.util.PasswordUtil;
import org.shamee.system.entity.SysUser;
import org.shamee.system.service.SysUserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 加盐密码编码器
 *
 * @author shamee
 * @since 2024-01-01
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SaltedPasswordEncoder implements PasswordEncoder {

    private final SysUserService sysUserService;

    @Override
    public String encode(CharSequence rawPassword) {
        // 这个方法主要用于新密码的编码，在实际使用中会配合盐值
        String[] saltAndPassword = PasswordUtil.generateSaltAndEncodePassword(rawPassword.toString());
        return saltAndPassword[1]; // 返回加密后的密码
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        // 这个方法需要获取用户的盐值来验证密码
        // 由于Spring Security的限制，这里需要通过其他方式获取盐值
        // 建议在AuthenticationProvider中处理密码验证
        log.warn("SaltedPasswordEncoder.matches方法被调用，建议使用自定义AuthenticationProvider");
        return false;
    }

    /**
     * 验证密码（带用户名）
     *
     * @param rawPassword 原始密码
     * @param username 用户名
     * @param encodedPassword 加密后的密码
     * @return 是否匹配
     */
    public boolean matches(CharSequence rawPassword, String username, String encodedPassword) {
        try {
            SysUser user = sysUserService.getUserByUsername(username);
            if (user == null || user.getSalt() == null) {
                log.warn("用户不存在或盐值为空: {}", username);
                return false;
            }
            return PasswordUtil.matches(rawPassword.toString(), user.getSalt(), encodedPassword);
        } catch (Exception e) {
            log.error("密码验证失败: {}", e.getMessage(), e);
            return false;
        }
    }
}