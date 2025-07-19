package org.shamee.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * 密码加盐工具类
 *
 * @author shamee
 * @since 2024-01-01
 */
@Slf4j
public class PasswordUtil {

    private static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    
    /**
     * 生成随机盐值
     *
     * @return 盐值
     */
    public static String generateSalt() {
        byte[] salt = new byte[16];
        SECURE_RANDOM.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }
    
    /**
     * 使用盐值加密密码
     *
     * @param rawPassword 原始密码
     * @param salt 盐值
     * @return 加密后的密码
     */
    public static String encodePassword(String rawPassword, String salt) {
        if (rawPassword == null || salt == null) {
            throw new IllegalArgumentException("密码和盐值不能为空");
        }
        // 将盐值与原始密码组合
        String saltedPassword = rawPassword + salt;
        return PASSWORD_ENCODER.encode(saltedPassword);
    }
    
    /**
     * 验证密码
     *
     * @param rawPassword 原始密码
     * @param salt 盐值
     * @param encodedPassword 加密后的密码
     * @return 是否匹配
     */
    public static boolean matches(String rawPassword, String salt, String encodedPassword) {
        if (rawPassword == null || salt == null || encodedPassword == null) {
            return false;
        }
        // 将盐值与原始密码组合
        String saltedPassword = rawPassword + salt;
        return PASSWORD_ENCODER.matches(saltedPassword, encodedPassword);
    }
    
    /**
     * 生成盐值并加密密码
     *
     * @param rawPassword 原始密码
     * @return 包含盐值和加密密码的数组 [salt, encodedPassword]
     */
    public static String[] generateSaltAndEncodePassword(String rawPassword) {
        String salt = generateSalt();
        String encodedPassword = encodePassword(rawPassword, salt);
        return new String[]{salt, encodedPassword};
    }
}