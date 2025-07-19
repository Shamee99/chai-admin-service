package org.shamee.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.shamee.common.entity.BaseEntity;

import java.util.List;

/**
 * 系统角色实体
 *
 * @author shamee
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_role")
public class SysRole extends BaseEntity {

    /**
     * 角色名称
     */
    @TableField("role_name")
    private String roleName;

    /**
     * 角色编码
     */
    @TableField("role_code")
    private String roleCode;

    /**
     * 角色描述
     */
    @TableField("role_desc")
    private String roleDesc;

    /**
     * 状态 0-禁用 1-启用
     */
    @TableField("status")
    private Integer status;

    /**
     * 排序
     */
    @TableField("sort_order")
    private Integer sortOrder;

    /**
     * 数据权限范围 1-全部数据 2-本部门及子部门数据 3-本部门数据 4-仅本人数据 5-自定义数据
     */
    @TableField("data_scope")
    private Integer dataScope;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    // ========== 非数据库字段 ==========

    /**
     * 菜单权限列表
     */
    @TableField(exist = false)
    private List<SysMenu> menus;

    /**
     * 菜单ID列表
     */
    @TableField(exist = false)
    private List<String> menuIds;

    /**
     * 权限标识列表
     */
    @TableField(exist = false)
    private List<String> permissions;

    /**
     * 数据权限部门ID列表（当dataScope为自定义时使用）
     */
    @TableField(exist = false)
    private List<String> deptIds;
}