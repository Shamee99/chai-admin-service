package org.shamee.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.shamee.common.entity.BaseEntity;

import java.util.List;

/**
 * 系统菜单实体
 *
 * @author shamee
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_menu", autoResultMap = true)
public class SysMenu extends BaseEntity {

    /**
     * 菜单名称
     */
    @TableField("menu_name")
    private String menuName;

    /**
     * 父菜单ID
     */
    @TableField("parent_id")
    private String parentId;

    /**
     * 菜单类型 1-目录 2-菜单 3-按钮
     */
    @TableField("menu_type")
    private Integer menuType;

    /**
     * 路由路径
     */
    @TableField("path")
    private String path;

    /**
     * 组件路径
     */
    @TableField("component")
    private String component;

    /**
     * 权限标识
     */
    private String permissions;

    /**
     * 菜单图标
     */
    @TableField("icon")
    private String icon;

    /**
     * 排序
     */
    @TableField("sort_order")
    private Integer sortOrder;

    /**
     * 状态 0-禁用 1-启用
     */
    @TableField("status")
    private Integer status;

    /**
     * 是否外链 0-否 1-是
     */
    @TableField("is_external")
    private Integer isExternal;

    /**
     * 是否缓存 0-否 1-是
     */
    @TableField("is_keepalive")
    private Integer isKeepAlive;

    /**
     * 是否显示 0-隐藏 1-显示
     */
    @TableField("is_visible")
    private Integer isVisible;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    // ========== 非数据库字段 ==========

    /**
     * 子菜单列表
     */
    @TableField(exist = false)
    private List<SysMenu> children;

}