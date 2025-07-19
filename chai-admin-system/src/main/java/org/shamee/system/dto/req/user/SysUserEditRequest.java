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
public class SysUserEditRequest {

    /**
     * 用户ID（更新时必填）
     */
    @NotNull(message = "用户ID不能为空")
    private String id;


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