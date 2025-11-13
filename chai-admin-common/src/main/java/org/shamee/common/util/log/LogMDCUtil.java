package org.shamee.common.util.log;

import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.core.text.StrUtil;
import org.shamee.common.dto.resp.UserPrincipal;
import org.shamee.common.util.context.SecurityUtils;
import org.slf4j.MDC;

/**
 * 日志MDC工具类
 * 用于在日志中添加用户信息（userId和nickName）
 * 
 * @author shamee
 * @date 2025-01-26
 */
@Slf4j
public class LogMDCUtil {
    
    /**
     * MDC中用户ID的键名
     */
    public static final String USER_ID_KEY = "userId";
    
    /**
     * MDC中用户昵称的键名
     */
    public static final String NICK_NAME_KEY = "nickName";
    
    /**
     * MDC中用户名的键名
     */
    public static final String USER_NAME_KEY = "userName";
    
    /**
     * 默认的系统用户ID
     */
    private static final String SYSTEM_USER_ID = "System";
    
    /**
     * 默认的系统用户昵称
     */
    private static final String SYSTEM_NICK_NAME = "系统";
    
    /**
     * 设置当前用户信息到MDC
     */
    public static void setUserInfo() {
        try {
            // 获取当前登录用户
            UserPrincipal currentUser = SecurityUtils.getUserInfo();
            if (currentUser != null) {
                // 设置用户ID
                String userId = currentUser.getUserId() != null ?
                        currentUser.getUserId() : SYSTEM_USER_ID;
                MDC.put(USER_ID_KEY, userId);
                
                // 设置用户昵称
                String nickName = StrUtil.isNotBlank(currentUser.getUsername()) ?
                    currentUser.getUsername() : SYSTEM_NICK_NAME;
                MDC.put(NICK_NAME_KEY, nickName);
                
                // 设置用户名
                String userName = StrUtil.isNotBlank(currentUser.getUsername()) ?
                    currentUser.getUsername() : SYSTEM_USER_ID;
                MDC.put(USER_NAME_KEY, userName);
                
                log.debug("设置MDC用户信息: userId={}, nickName={}, userName={}", 
                    userId, nickName, userName);
            } else {
                setSystemUserInfo();
            }
        } catch (Exception e) {
            // 如果获取用户信息失败，设置为系统用户
            log.debug("获取用户信息失败，设置为系统用户: {}", e.getMessage());
            setSystemUserInfo();
        }
    }
    
    /**
     * 设置系统用户信息到MDC
     */
    public static void setSystemUserInfo() {
        MDC.put(USER_ID_KEY, SYSTEM_USER_ID);
        MDC.put(NICK_NAME_KEY, SYSTEM_NICK_NAME);
        MDC.put(USER_NAME_KEY, SYSTEM_USER_ID);
    }
    
    /**
     * 设置指定用户信息到MDC
     * 
     * @param userId 用户ID
     * @param nickName 用户昵称
     * @param userName 用户名
     */
    public static void setUserInfo(String userId, String nickName, String userName) {
        MDC.put(USER_ID_KEY, StrUtil.isNotBlank(userId) ? userId : SYSTEM_USER_ID);
        MDC.put(NICK_NAME_KEY, StrUtil.isNotBlank(nickName) ? nickName : SYSTEM_NICK_NAME);
        MDC.put(USER_NAME_KEY, StrUtil.isNotBlank(userName) ? userName : SYSTEM_USER_ID);
    }

    /**
     * 获取当前MDC中的用户ID
     * 
     * @return 用户ID
     */
    public static String getUserId() {
        return MDC.get(USER_ID_KEY);
    }
    
    /**
     * 获取当前MDC中的用户昵称
     * 
     * @return 用户昵称
     */
    public static String getNickName() {
        return MDC.get(NICK_NAME_KEY);
    }
    
    /**
     * 获取当前MDC中的用户名
     * 
     * @return 用户名
     */
    public static String getUserName() {
        return MDC.get(USER_NAME_KEY);
    }
    
    /**
     * 清除MDC中的用户信息
     */
    public static void clearUserInfo() {
        MDC.remove(USER_ID_KEY);
        MDC.remove(NICK_NAME_KEY);
        MDC.remove(USER_NAME_KEY);
        log.debug("清除MDC用户信息");
    }
    
    /**
     * 清除所有MDC信息
     */
    public static void clearAll() {
        MDC.clear();
        log.debug("清除所有MDC信息");
    }
    
    /**
     * 检查是否已设置用户信息
     * 
     * @return 是否已设置用户信息
     */
    public static boolean hasUserInfo() {
        return StrUtil.isNotBlank(MDC.get(USER_ID_KEY));
    }
    
    /**
     * 获取用户信息摘要（用于日志输出）
     * 
     * @return 用户信息摘要
     */
    public static String getUserInfoSummary() {
        String userId = getUserId();
        String nickName = getNickName();
        
        if (StrUtil.isBlank(userId)) {
            return "未知用户";
        }
        
        if (StrUtil.isNotBlank(nickName) && !nickName.equals(userId)) {
            return String.format("%s(%s)", nickName, userId);
        } else {
            return userId;
        }
    }
}
