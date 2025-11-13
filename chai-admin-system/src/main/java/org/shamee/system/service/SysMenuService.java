package org.shamee.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.shamee.common.dto.req.PageRequest;
import org.shamee.common.dto.resp.PageResult;
import org.shamee.system.dto.req.menu.SysMenuEditRequest;
import org.shamee.system.dto.req.menu.SysMenuQueryRequest;
import org.shamee.system.dto.resp.menu.SysMenuPageResp;
import org.shamee.system.dto.resp.menu.SysMenuTreeResp;
import org.shamee.system.entity.SysMenu;
import org.shamee.system.dto.req.menu.SysMenuSaveRequest;

import java.util.List;

/**
 * 系统菜单服务接口
 *
 * @author shamee
 * @since 2024-01-01
 */
public interface SysMenuService extends IService<SysMenu> {

    /**
     * 获取菜单树形列表
     *
     * @return 菜单树形列表
     */
    List<SysMenuTreeResp> getMenuTree();

    /**
     * 获取所有菜单列表（包含按钮）
     *
     * @return 菜单列表
     */
    PageResult<SysMenuPageResp> getMenuPage(PageRequest<SysMenuQueryRequest> pageRequest);

    /**
     * 根据用户ID获取菜单树形列表
     *
     * @param userId 用户ID
     * @return 菜单树形列表
     */
    List<SysMenu> getUserMenuTree(String userId);

    /**
     * 根据角色ID获取菜单列表
     *
     * @param roleId 角色ID
     * @return 菜单列表
     */
    List<SysMenu> getMenusByRoleId(String roleId);

    /**
     * 根据菜单ID获取菜单详情
     *
     * @param menuId 菜单ID
     * @return 菜单详情
     */
    SysMenu getMenuById(String menuId);

    /**
     * 保存菜单信息
     *
     * @param request 菜单信息
     * @return 是否成功
     */
    boolean saveMenu(SysMenuSaveRequest request);

    /**
     * 编辑菜单信息
     *
     * @param request 菜单信息
     * @return 是否成功
     */
    boolean editMenu(SysMenuEditRequest request);

    /**
     * 更新菜单状态
     *
     * @param menuId 菜单ID
     * @param status 状态
     * @return 是否成功
     */
    boolean updateMenuStatus(String menuId, Integer status);

    /**
     * 删除菜单
     *
     * @param menuId 菜单ID
     * @return 是否成功
     */
    boolean deleteMenu(String menuId);

    /**
     * 检查菜单名称是否存在（同一级别下）
     *
     * @param menuName 菜单名称
     * @param parentId 父菜单ID
     * @param excludeId 排除的菜单ID
     * @return 是否存在
     */
    boolean checkMenuNameExists(String menuName, String parentId, String excludeId);

    /**
     * 检查权限标识是否存在
     *
     * @param permission 权限标识
     * @param excludeId 排除的菜单ID
     * @return 是否存在
     */
    boolean checkPermissionExists(String permission, String excludeId);

    /**
     * 构建菜单树形结构
     *
     * @param menus 菜单列表
     * @return 树形菜单列表
     */
    List<SysMenu> buildMenuTree(List<SysMenu> menus);

    /**
     * 根据用户ID获取权限标识列表
     *
     * @param userId 用户ID
     * @return 权限标识列表
     */
    List<String> getUserPermissions(String userId);

}