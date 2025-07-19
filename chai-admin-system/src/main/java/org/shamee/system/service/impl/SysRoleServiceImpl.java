package org.shamee.system.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.shamee.common.constant.CommonConstant;
import org.shamee.common.dto.req.PageRequest;
import org.shamee.common.dto.resp.PageResult;
import org.shamee.common.util.copy.BeanCopierUtils;
import org.shamee.system.dto.req.role.SysRoleEditRequest;
import org.shamee.system.dto.req.role.SysRoleQueryRequest;
import org.shamee.system.dto.req.role.SysRoleSaveRequest;
import org.shamee.system.dto.resp.role.SysRolePageResp;
import org.shamee.system.entity.SysRole;
import org.shamee.system.entity.SysRoleDept;
import org.shamee.system.entity.SysRoleMenu;
import org.shamee.system.entity.SysUserRole;
import org.shamee.system.mapper.SysRoleDeptMapper;
import org.shamee.system.mapper.SysRoleMapper;
import org.shamee.system.mapper.SysRoleMenuMapper;
import org.shamee.system.service.SysRoleDeptService;
import org.shamee.system.service.SysRoleMenuService;
import org.shamee.system.service.SysRoleService;
import org.shamee.system.service.SysUserRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 系统角色服务实现类
 *
 * @author shamee
 * @since 2024-01-01
 */
@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    private final SysRoleMenuService sysRoleMenuService;
    private final SysRoleDeptService sysRoleDeptService;
    private final SysUserRoleService sysUserRoleService;

    @Override
    public PageResult<SysRolePageResp> getRolePage(PageRequest<SysRoleQueryRequest> request) {
        Page<SysRole> page = new Page<>(request.getPageNo(), request.getPageSize());
        Page<SysRole> ipage = this.lambdaQuery()
                .like(StrUtil.isNotBlank(request.getParam().getRoleCode()), SysRole::getRoleCode, request.getParam().getRoleCode())
                .like(StrUtil.isNotBlank(request.getParam().getRoleName()), SysRole::getRoleName, request.getParam().getRoleName())
                .eq(request.getParam().getStatus() != null, SysRole::getStatus, request.getParam().getStatus())
                .page(page);

        if (ipage == null || CollUtil.isEmpty(ipage.getRecords())) {
            return PageResult.empty(SysRolePageResp.class);
        }

        return PageResult.of(ipage, SysRolePageResp::new);
    }

    @Override
    public List<SysRolePageResp> getRoleList(SysRoleQueryRequest request) {
        List<SysRole> listed = this.lambdaQuery().like(StrUtil.isNotBlank(request.getRoleCode()), SysRole::getRoleCode, request.getRoleCode())
                .like(StrUtil.isNotBlank(request.getRoleName()), SysRole::getRoleName, request.getRoleName())
                .eq(request.getStatus() != null, SysRole::getStatus, request.getStatus())
                .list();
        if(CollUtil.isEmpty(listed)) {
            return List.of();
        }
        return BeanCopierUtils.copyListProperties(listed, SysRolePageResp::new);
    }


    @Override
    public List<SysRole> getRolesByUserId(String userId) {
        return baseMapper.selectByUserId(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveRole(SysRoleSaveRequest request) {
        SysRole role = BeanCopierUtils.copy(request, SysRole::new);
        // 新增角色
        // 检查角色编码是否重复
        if (StringUtils.hasText(request.getRoleCode()) &&
            checkRoleCodeExists(request.getRoleCode(), null)) {
            throw new RuntimeException("角色编码已存在");
        }

        // 检查角色名称是否重复
        if (checkRoleNameExists(request.getRoleName(), null)) {
            throw new RuntimeException("角色名称已存在");
        }

        return save(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean editRole(SysRoleEditRequest request) {
        // 更新角色
        SysRole existRole = getById(request.getId());
        if (existRole == null) {
            throw new RuntimeException("角色不存在");
        }

        // 检查角色编码是否重复
        if (StringUtils.hasText(request.getRoleCode()) &&
            checkRoleCodeExists(request.getRoleCode(), request.getId())) {
            throw new RuntimeException("角色编码已存在");
        }

        // 检查角色名称是否重复
        if (checkRoleNameExists(request.getRoleName(), request.getId())) {
            throw new RuntimeException("角色名称已存在");
        }

        return updateById(BeanCopierUtils.copy(request, SysRole::new));
    }

    @Override
    public boolean updateRoleStatus(String roleId, Integer status) {
        SysRole role = new SysRole();
        role.setId(roleId);
        role.setStatus(status);
        return updateById(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRole(String roleId) {
        // 删除角色关联的菜单
        sysRoleMenuService.lambdaUpdate().eq(SysRoleMenu::getRoleId, roleId).remove();
        // 删除角色关联的部门
        sysRoleDeptService.lambdaUpdate().eq(SysRoleDept::getRoleId, roleId).remove();
        // 删除关联用户信息
        sysUserRoleService.lambdaUpdate().eq(SysUserRole::getRoleId, roleId).remove();
        // 删除角色
        return removeById(roleId);
    }


    @Override
    public boolean checkRoleCodeExists(String roleCode, String excludeId) {
        return this.count(
                Wrappers.lambdaQuery(SysRole.class).eq(SysRole::getRoleCode, roleCode)
                        .ne(excludeId != null, SysRole::getId, excludeId)
        ) > 0;
    }

    @Override
    public boolean checkRoleNameExists(String roleName, String excludeId) {
        return this.count(
                Wrappers.lambdaQuery(SysRole.class).eq(SysRole::getRoleName, roleName)
                        .ne(excludeId != null, SysRole::getId, excludeId)
        ) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignPerms(String roleId, List<String> permissionIds) {
        // 先清空角色原有的数据
        SysRole role = this.getById(roleId);
        Objects.requireNonNull(role, "该角色不存在，请重新确认");

        // 清空角色原有的权限
        sysRoleMenuService.deleteRoleMenuByRoleId(roleId);

        // 这里需要将权限的父节点一起给到


        // 保存权限数据
        List<SysRoleMenu> sysRoleMenus = permissionIds.stream().map(perm -> SysRoleMenu.builder().roleId(roleId).menuId(perm).build()).toList();
        if(CollUtil.isNotEmpty(sysRoleMenus)) {
            sysRoleMenuService.saveBatch(sysRoleMenus);
        }
        return true;
    }

    @Override
    public List<String> getRolePermissions(String roleId) {
        List<SysRoleMenu> list = sysRoleMenuService.lambdaQuery().eq(SysRoleMenu::getRoleId, roleId).select(SysRoleMenu::getMenuId).list();
        if(CollUtil.isEmpty(list)) {
            return List.of();
        }
        return list.stream().map(SysRoleMenu::getMenuId).collect(Collectors.toList());
    }


}