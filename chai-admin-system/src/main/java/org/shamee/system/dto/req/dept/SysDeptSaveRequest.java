package org.shamee.system.dto.req.dept;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * 系统部门保存请求
 *
 * @author shamee
 * @since 2024-01-01
 */
@Data
public class SysDeptSaveRequest {

    /**
     * 部门名称
     */
    @NotBlank(message = "部门名称不能为空")
    private String deptName;

    /**
     * 父部门ID
     */
    private String parentId;

    /**
     * 部门编码
     */
    @NotBlank(message = "部分编码不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9_]{2,20}$", message = "部门编码只能包含字母、数字、下划线，长度2-20位")
    private String deptCode;

    /**
     * 负责人
     */
    private String leader;

    /**
     * 联系电话
     */
//    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    /**
     * 邮箱
     */
//    @Email(message = "邮箱格式不正确")
    private String email;

    /**
     * 排序
     */
    private Integer sortOrder;

    /**
     * 状态 0-禁用 1-启用
     */
    @NotNull(message = "状态不能为空")
    private Integer status;

    /**
     * 备注
     */
    private String remark;
}