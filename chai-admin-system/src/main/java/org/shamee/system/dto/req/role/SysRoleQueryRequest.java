package org.shamee.system.dto.req.role;

import lombok.Data;

/**
 * 系统角色查询请求
 *
 * @author shamee
 * @since 2024-01-01
 */
@Data
public class SysRoleQueryRequest {

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色编码
     */
    private String roleCode;

    /**
     * 状态 0-禁用 1-启用
     */
    private Integer status;
}