package org.shamee.system.dto.req.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 登录请求
 *
 * @author shamee
 * @since 2024-01-01
 */
@Data
public class LoginRequest {

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    private String username;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;

    /**
     * 验证码
     */
    private String captcha;

    /**
     * 验证码Key
     */
    private String captchaKey;

    /**
     * 记住我
     */
    private Boolean rememberMe = false;
}