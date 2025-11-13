package org.shamee.common.util.context;


import org.shamee.common.dto.resp.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 获取用户上下文信息
 * @author shamee
 */
public class SecurityUtils {

    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static UserPrincipal getUserInfo() {
        return (UserPrincipal) getAuthentication().getPrincipal();
    }

    public static String getUserId() {
        return getUserInfo().getUserId();
    }

    public static String getUserName() {
        return getUserInfo().getUsername();
    }

}
