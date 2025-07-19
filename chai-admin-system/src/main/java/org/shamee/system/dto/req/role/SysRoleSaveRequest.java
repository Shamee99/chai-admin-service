package org.shamee.system.dto.req.role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * 系统角色保存请求
 *
 * @author shamee
 * @since 2024-01-01
 */
@Data
public class SysRoleSaveRequest {


    /**
     * 角色名称
     */
    @NotBlank(message = "角色名称不能为空")
    private String roleName;

    /**
     * 角色编码
     */
    @NotBlank(message = "角色编码不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9_]{2,20}$", message = "角色编码只能包含字母、数字、下划线，长度2-20位")
    private String roleCode;

    /**
     * 角色描述
     */
    private String roleDesc;

    /**
     * 状态 0-禁用 1-启用
     */
    @NotNull(message = "状态不能为空")
    private Integer status;

    /**
     * 排序
     */
    private Integer sortOrder;

    /**
     * 数据权限范围 1-全部数据 2-本部门及子部门数据 3-本部门数据 4-仅本人数据 5-自定义数据
     */
//    @NotNull(message = "数据权限范围不能为空")
    private Integer dataScope;

    /**
     * 备注
     */
    private String remark;
}