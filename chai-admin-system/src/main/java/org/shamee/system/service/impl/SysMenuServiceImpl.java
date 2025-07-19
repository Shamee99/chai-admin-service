package org.shamee.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.shamee.common.constant.CommonConstant;
import org.shamee.common.dto.req.PageRequest;
import org.shamee.common.dto.resp.PageResult;
import org.shamee.common.util.context.SecurityUtils;
import org.shamee.common.util.copy.BeanCopierUtils;
import org.shamee.system.dto.req.menu.SysMenuEditRequest;
import org.shamee.system.dto.req.menu.SysMenuQueryRequest;
import org.shamee.system.dto.req.menu.SysMenuSaveRequest;
import org.shamee.system.dto.resp.menu.SysMenuPageResp;
import org.shamee.system.dto.resp.menu.SysMenuTreeResp;
import org.shamee.system.entity.SysMenu;
import org.shamee.system.entity.SysRoleMenu;
import org.shamee.system.mapper.SysMenuMapper;
import org.shamee.system.service.SysMenuService;
import org.shamee.system.service.SysRoleMenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 系统菜单服务实现类
 *
 * @author shamee
 * @since 2024-01-01
 */
@Service
@RequiredArgsConstructor
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    private final SysRoleMenuService sysRoleMenuService;

    @Override
    public List<SysMenuTreeResp> getMenuTree() {
        List<SysMenu> allMenus = list(new LambdaQueryWrapper<SysMenu>()
            .select(SysMenu::getId, SysMenu::getParentId, SysMenu::getMenuType, SysMenu::getMenuName, SysMenu::getIcon)
            .orderByAsc(SysMenu::getSortOrder));
        return buildMenuTreeResult(allMenus);
    }

    @Override
    public PageResult<SysMenuPageResp> getMenuPage(PageRequest<SysMenuQueryRequest> request) {
        Page<SysMenu> page = new Page<>(request.getPageNo(), request.getPageSize());
        Page<SysMenu> ipage = this.lambdaQuery()
                .like(StrUtil.isNotBlank(request.getParam().getMenuName()), SysMenu::getMenuName, request.getParam().getMenuName())
                .eq(request.getParam().getStatus() != null, SysMenu::getStatus, request.getParam().getStatus())
                .eq(request.getParam().getParentId() != null, SysMenu::getParentId, request.getParam().getParentId())
                .page(page);

        if (ipage == null || CollUtil.isEmpty(ipage.getRecords())) {
            return PageResult.empty(SysMenuPageResp.class);
        }
        return PageResult.of(ipage, SysMenuPageResp::new);
    }

    @Override
    public List<SysMenu> getUserMenuTree(String userId) {
        if(SecurityUtils.getUserName().equalsIgnoreCase(CommonConstant.SUPER_ADMIN)) {
            return this.lambdaQuery().ne(SysMenu::getMenuType, 3)
                    .eq(SysMenu::getIsVisible, 1)
                    .list();
        }
        return baseMapper.selectByUserId(userId);
    }

    @Override
    public List<SysMenu> getMenusByRoleId(String roleId) {
        return baseMapper.selectByRoleId(roleId);
    }

    @Override
    public SysMenu getMenuById(String menuId) {
        return getById(menuId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveMenu(SysMenuSaveRequest request) {
        SysMenu menu = BeanCopierUtils.copy(request, SysMenu::new, (s, t) -> {
            if(StrUtil.isNotBlank(s.getPermission())) {
                t.setPermissions(s.getPermission());
            }
        });
        
        // 新增菜单
        // 检查菜单名称是否重复
        if (checkMenuNameExists(request.getMenuName(), request.getParentId(), null)) {
            throw new RuntimeException("同级菜单名称已存在");
        }

        // 检测一下当前菜单的上一级是否也是菜单。如果是的话不能新增。当前仅支持2级菜单
        if(StrUtil.isBlankIfStr(menu.getParentId())) {
            return save(menu);
        }

        boolean validMenu = this.count(Wrappers.lambdaQuery(SysMenu.class)
                .eq(SysMenu::getId, menu.getParentId())
                .ne(SysMenu::getMenuType, 1)) > 0;
        if(validMenu) {
            throw new IllegalCallerException("当前仅支持二级菜单");
        }
        return save(menu);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean editMenu(SysMenuEditRequest request) {
        SysMenu menu = BeanCopierUtils.copy(request, SysMenu::new, (s, t) -> {
            t.setPermissions(s.getPermission());
        });

        SysMenu sysMenu = this.getById(request.getId());
        Objects.requireNonNull(sysMenu, "菜单不存在");

        // 检查菜单名称是否重复
        if (checkMenuNameExists(request.getMenuName(), request.getParentId(), request.getId())) {
            throw new RuntimeException("同级菜单名称已存在");
        }

        return updateById(menu);
    }

    @Override
    public boolean updateMenuStatus(String menuId, Integer status) {
        SysMenu menu = new SysMenu();
        menu.setId(menuId);
        menu.setStatus(status);
        return updateById(menu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteMenu(String menuId) {
        // 检查是否有子菜单
        if (this.count(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getParentId, menuId)) > 0) {
            throw new RuntimeException("存在子菜单，无法删除");
        }

        // 检查是否有角色关联此菜单
        if (sysRoleMenuService.count(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getMenuId, menuId)) > 0) {
            throw new RuntimeException("该菜单已分配给角色，无法删除");
        }
        
        // 删除菜单
        return removeById(menuId);
    }

    @Override
    public boolean checkMenuNameExists(String menuName, String parentId, String excludeId) {
        return this.count(new QueryWrapper<SysMenu>().lambda()
                .eq(SysMenu::getMenuName, menuName)
                .eq(SysMenu::getParentId, parentId)
                .ne(excludeId != null, SysMenu::getId, excludeId)
        ) > 0;
    }

    @Override
    public boolean checkPermissionExists(String permission, String excludeId) {
//        return this.count(new QueryWrapper<SysMenu>().lambda()
//                .eq(SysMenu::getPermissions, menuName)
//                .eq(SysMenu::getParentId, parentId)
//                .ne(excludeId != null, SysMenu::getId, excludeId)
//        ) > 0;
        return false;
    }

    @Override
    public List<SysMenu> buildMenuTree(List<SysMenu> menus) {
        if (CollectionUtils.isEmpty(menus)) {
            return new ArrayList<>();
        }
        
        List<SysMenu> rootMenus = new ArrayList<>();
        
        for (SysMenu menu : menus) {
            if (menu.getParentId() == null || menu.getParentId().equalsIgnoreCase("0")) {
                // 根菜单
                menu.setChildren(getChildMenus(menu.getId(), menus));
                rootMenus.add(menu);
            }
        }
        
        return rootMenus;
    }

    private List<SysMenuTreeResp> buildMenuTreeResult(List<SysMenu> menus) {
        if (CollectionUtils.isEmpty(menus)) {
            return List.of();
        }

        List<SysMenuTreeResp> rootMenus = new ArrayList<>();

        for (SysMenu menu : menus) {
            if (menu.getParentId() == null || menu.getParentId().equalsIgnoreCase("0")) {
                // 根菜单
                menu.setChildren(getChildMenus(menu.getId(), menus));
                rootMenus.add(BeanCopierUtils.copy(menu, SysMenuTreeResp::new));
            }
        }

        return rootMenus;
    }
    
    /**
     * 递归获取子菜单
     */
    private List<SysMenu> getChildMenus(String parentId, List<SysMenu> allMenus) {
        List<SysMenu> childMenus = allMenus.stream()
            .filter(menu -> parentId.equals(menu.getParentId()))
            .collect(Collectors.toList());
        
        for (SysMenu childMenu : childMenus) {
            childMenu.setChildren(getChildMenus(childMenu.getId(), allMenus));
        }
        
        return childMenus;
    }

    @Override
    public List<String> getUserPermissions(String userId) {
        return baseMapper.selectPermissionsByUserId(userId);
    }
}