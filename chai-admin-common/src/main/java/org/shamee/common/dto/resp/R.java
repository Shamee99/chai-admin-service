package org.shamee.common.dto.resp;

import lombok.Data;

/**
 * 统一响应结果类
 *
 * @author shamee
 * @since 2024-01-01
 */
@Data
public class R<T> {

    /**
     * 响应码
     */
    private Integer code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 时间戳
     */
    private Long timestamp;

    private boolean success;

    public R() {
        this.timestamp = System.currentTimeMillis();
    }

    public R(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 成功响应
     */
    public static <T> R<T> success() {
        return new R<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), null);
    }

    /**
     * 成功响应（带数据）
     */
    public static <T> R<T> success(T data) {
        return new R<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    /**
     * 成功响应（自定义消息）
     */
    public static <T> R<T> success(String message, T data) {
        return new R<>(ResultCode.SUCCESS.getCode(), message, data);
    }

    /**
     * 失败响应
     */
    public static <T> R<T> error() {
        return new R<>(ResultCode.ERROR.getCode(), ResultCode.ERROR.getMessage(), null);
    }

    /**
     * 失败响应（自定义消息）
     */
    public static <T> R<T> error(String message) {
        return new R<>(ResultCode.ERROR.getCode(), message, null);
    }

    /**
     * 失败响应（自定义码和消息）
     */
    public static <T> R<T> error(Integer code, String message) {
        return new R<>(code, message, null);
    }

    public static <T> R<T> error(ResultCode code, String message) {
        return new R<>(code.getCode(), message, null);
    }

    /**
     * 根据结果码构建响应
     */
    public static <T> R<T> result(ResultCode resultCode) {
        return new R<>(resultCode.getCode(), resultCode.getMessage(), null);
    }

    /**
     * 根据结果码构建响应（带数据）
     */
    public static <T> R<T> result(ResultCode resultCode, T data) {
        return new R<>(resultCode.getCode(), resultCode.getMessage(), data);
    }

    public boolean isSuccess() {
        return this.code != null && this.code.equals(ResultCode.SUCCESS.getCode());
    }
}