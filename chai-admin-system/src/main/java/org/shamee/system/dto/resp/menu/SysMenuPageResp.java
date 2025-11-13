package org.shamee.system.dto.resp.menu;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SysMenuPageResp {

    private String id;
    private String menuName;

    private String parentId;

    /**
     * 菜单类型 1-目录 2-菜单 3-按钮
     */
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
    private String permissions;

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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
