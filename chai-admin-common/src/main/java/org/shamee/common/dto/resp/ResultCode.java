package org.shamee.common.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 响应状态码枚举
 *
 * @author shamee
 * @since 2024-01-01
 */
@Getter
@AllArgsConstructor
public enum ResultCode {

    /**
     * 成功
     */
    SUCCESS(200, "操作成功"),

    /**
     * 失败
     */
    ERROR(500, "操作失败"),

    /**
     * 参数错误
     */
    PARAM_ERROR(400, "参数错误"),

    /**
     * 未授权
     */
    UNAUTHORIZED(401, "未授权"),

    /**
     * 禁止访问
     */
    FORBIDDEN(403, "禁止访问"),

    /**
     * 资源不存在
     */
    NOT_FOUND(404, "资源不存在"),

    /**
     * 用户不存在
     */
    USER_NOT_FOUND(1001, "用户不存在"),

    /**
     * 用户名或密码错误
     */
    LOGIN_ERROR(1002, "用户名或密码错误"),

    /**
     * 用户已被禁用
     */
    USER_DISABLED(1003, "用户已被禁用"),

    /**
     * 角色不存在
     */
    ROLE_NOT_FOUND(2001, "角色不存在"),

    /**
     * 权限不足
     */
    PERMISSION_DENIED(2002, "权限不足"),

    /**
     * 菜单不存在
     */
    MENU_NOT_FOUND(3001, "菜单不存在"),

    /**
     * 部门不存在
     */
    DEPT_NOT_FOUND(4001, "部门不存在"),

    /**
     * 系统错误
     */
    SYSTEM_ERROR(5000, "系统错误"),

    /**
     * 令牌无效
     */
    TOKEN_INVALID(5001, "令牌无效"),

    /**
     * 令牌过期
     */
    TOKEN_EXPIRED(5002, "令牌过期"),

    /**
     * 账户被锁定
     */
    ACCOUNT_LOCKED(5003, "账户被锁定");

    /**
     * 状态码
     */
    private final Integer code;

    /**
     * 消息
     */
    private final String message;
}