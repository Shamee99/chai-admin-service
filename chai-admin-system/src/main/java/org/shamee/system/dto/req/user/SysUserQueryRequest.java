package org.shamee.system.dto.req.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 系统用户查询请求
 *
 * @author shamee
 * @since 2024-01-01
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    private List<String> userIds;

}