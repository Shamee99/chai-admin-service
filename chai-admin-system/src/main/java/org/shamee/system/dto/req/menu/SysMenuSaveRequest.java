package org.shamee.system.dto.req.menu;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 系统菜单保存请求
 *
 * @author shamee
 * @since 2024-01-01
 */
@Data
public class SysMenuSaveRequest  {

    /**
     * 菜单名称
     */
    @NotBlank(message = "菜单名称不能为空")
    private String menuName;

    /**
     * 父菜单ID
     */
    private String parentId;

    /**
     * 菜单类型 1-目录 2-菜单 3-按钮
     */
    @NotNull(message = "菜单类型不能为空")
    private Integer menuType;

    /**
     * 路由路径
     */
    private String path;

    /**
     * 组件路径
     */
    private String component;

    /**
     * 权限标识
     */
    private String permission;

    /**
     * 菜单图标
     */
    private String icon;

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
     * 是否外链 0-否 1-是
     */
    private Integer isExternal;

    /**
     * 是否缓存 0-否 1-是
     */
    private Integer isKeepAlive;

    /**
     * 是否显示 0-隐藏 1-显示
     */
    private Integer isVisible;

    /**
     * 备注
     */
    private String remark;
}