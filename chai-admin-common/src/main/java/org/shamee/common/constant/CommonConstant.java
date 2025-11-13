package org.shamee.common.constant;

/**
 * 通用常量
 *
 * @author shamee
 * @since 2024-01-01
 */
public interface CommonConstant {

    /**
     * 状态
     */
    interface Status {
        /**
         * 启用
         */
        Integer ENABLED = 1;
        /**
         * 禁用
         */
        Integer DISABLED = 0;
    }

    /**
     * 性别
     */
    interface Gender {
        /**
         * 男
         */
        Integer MALE = 1;
        /**
         * 女
         */
        Integer FEMALE = 2;
        /**
         * 未知
         */
        Integer UNKNOWN = 0;
    }

    /**
     * 菜单类型
     */
    interface MenuType {
        /**
         * 目录
         */
        Integer CATALOG = 1;
        /**
         * 菜单
         */
        Integer MENU = 2;
        /**
         * 按钮
         */
        Integer BUTTON = 3;
    }

    /**
     * 是否
     */
    interface YesNo {
        /**
         * 是
         */
        Integer YES = 1;
        /**
         * 否
         */
        Integer NO = 0;
    }

    String SUPER_ADMIN = "superAdmin";

    /**
     * 超级管理员角色编码
     */
    String SUPER_ADMIN_ROLE_CODE = "super_admin";

    /**
     * 默认密码
     */
    String DEFAULT_PASSWORD = "123456";

    /**
     * JWT相关常量
     */
    interface Jwt {
        /**
         * JWT密钥
         */
        String SECRET = "chai-admin-jwt-secret-key-2024-secure-256-bits";
        
        /**
         * JWT过期时间（毫秒）- 24小时
         */
        Long EXPIRATION = 24 * 60 * 60 * 1000L;
        
        /**
         * 刷新Token过期时间（毫秒）- 7天
         */
        Long REFRESH_EXPIRATION = 7 * 24 * 60 * 60 * 1000L;
        
        /**
         * Token前缀
         */
        String TOKEN_PREFIX = "Bearer ";
        
        /**
         * Token请求头
         */
        String TOKEN_HEADER = "Authorization";
        
        /**
         * 用户ID声明
         */
        String CLAIM_USER_ID = "userId";
        
        /**
         * 用户名声明
         */
        String CLAIM_USERNAME = "username";
        
        /**
         * 权限声明
         */
        String CLAIM_AUTHORITIES = "authorities";
    }

    /**
     * Redis缓存Key前缀
     */
    interface RedisKey {
        /**
         * 用户Token缓存前缀
         */
        String USER_TOKEN = "user:token:";
        
        /**
         * 用户权限缓存前缀
         */
        String USER_PERMISSIONS = "user:permissions:";
        
        /**
         * 用户信息缓存前缀
         */
        String USER_INFO = "user:info:";
        
        /**
         * 登录失败次数缓存前缀
         */
        String LOGIN_FAIL_COUNT = "login:fail:count:";
        
        /**
         * 账户锁定缓存前缀
         */
        String ACCOUNT_LOCK = "account:lock:";
    }
}