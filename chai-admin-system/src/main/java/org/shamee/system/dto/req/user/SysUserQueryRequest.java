package org.shamee.system.dto.req.user;

import lombok.Data;

/**
 * 系统用户查询请求
 *
 * @author shamee
 * @since 2024-01-01
 */
@Data
public class SysUserQueryRequest {

    /**
     * 用户名
     */
    private String username;


    /**
     * 手机号
     */
    private String phone;

    /**
     * 状态 0-禁用 1-启用
     */
    private Integer status;

    /**
     * 部门ID
     */
    private String deptId;

}