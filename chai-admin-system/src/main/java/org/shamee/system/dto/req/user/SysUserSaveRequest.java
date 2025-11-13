package org.shamee.system.dto.req.user;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.shamee.common.validator.OptionalPattern;

import java.util.List;

/**
 * 系统用户保存请求
 *
 * @author shamee
 * @since 2024-01-01
 */
@Data
public class SysUserSaveRequest {

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9_]{4,20}$", message = "用户名只能包含字母、数字、下划线，长度4-20位")
    private String username;

    /**
     * 密码（新增时必填）
     */
    private String password;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 昵称
     */
    @NotBlank(message = "昵称不能为空")
    private String nickname;

    /**
     * 邮箱
     */
    @OptionalPattern(checks = Email.class, message = "邮箱格式不正确")
    @Email
    private String email;

    /**
     * 手机号
     */
    @OptionalPattern(pattern = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    /**
     * 性别 0-未知 1-男 2-女
     */
    private Integer gender;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 状态 0-禁用 1-启用
     */
    @NotNull(message = "状态不能为空")
    private Integer status;

    /**
     * 部门ID
     */
    @NotNull(message = "部门不能为空")
    private String deptId;

    /**
     * 角色ID列表
     */
    @NotEmpty(message = "角色不能为空")
    private List<String> roleIds;

    /**
     * 备注
     */
    private String remark;
}