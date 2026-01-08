package org.shamee.system.security;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.core.collection.ListUtil;
import org.shamee.common.constant.CommonConstant;
import org.shamee.common.dto.resp.UserPrincipal;
import org.shamee.system.entity.SysUser;
import org.shamee.system.service.SysUserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 用户详情服务实现
 *
 * @author shamee
 * @since 2024-01-01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final SysUserService sysUserService;

    @Override
    public UserDetails loadUserByUsername(String username)
        throws UsernameNotFoundException {
        log.debug("加载用户信息: {}", username);

        // 根据用户名查询用户信息
        SysUser user = sysUserService.getUserByUsername(username);
        if (user == null) {
            log.warn("用户不存在: {}", username);
            throw new UsernameNotFoundException("用户不存在: " + username);
        }

        // 检查用户状态
        if (!CommonConstant.Status.ENABLED.equals(user.getStatus())) {
            log.warn("用户已被禁用: {}", username);
            throw new UsernameNotFoundException("用户已被禁用: " + username);
        }

        // 检查删除标识
        if (Boolean.TRUE.equals(user.getDeleted())) {
            log.warn("用户已被删除: {}", username);
            throw new UsernameNotFoundException("用户已被删除: " + username);
        }

        // 获取用户权限
        List<String> permissions = sysUserService.getUserPermissions(
            user.getId()
        );

        // 构建UserPrincipal
        UserPrincipal userPrincipal = new UserPrincipal();
        userPrincipal.setUserId(user.getId());
        userPrincipal.setUsername(user.getUsername());
        userPrincipal.setPassword(user.getPassword());
        userPrincipal.setRealName(user.getRealName());
        userPrincipal.setEmail(user.getEmail());
        userPrincipal.setPhone(user.getPhone());
        userPrincipal.setDeptId(user.getDeptId());
        userPrincipal.setStatus(user.getStatus());
        userPrincipal.setPermissions(permissions);

        // 设置角色信息（从权限中提取角色）
        List<String> roles = permissions
            .stream()
            .filter(permission -> permission.startsWith("ROLE_"))
            .toList();
        userPrincipal.setRoles(roles);

        // 设置数据权限
        setDataScopeForUser(userPrincipal, user.getId(), roles);

        log.debug(
            "用户信息加载完成: {}, 权限数量: {}",
            username,
            permissions.size()
        );
        return userPrincipal;
    }

    /**
     * 设置用户的数据权限
     * 根据用户的角色计算数据权限范围（取最小权限）
     *
     * @param userPrincipal 用户主体
     * @param userId 用户ID
     * @param roles 角色列表
     */
    private void setDataScopeForUser(
        UserPrincipal userPrincipal,
        String userId,
        List<String> roles
    ) {
        if (roles == null || roles.isEmpty()) {
            // 没有角色，设置默认权限
            userPrincipal.setDataScope(4); // 仅本人数据
            userPrincipal.setDataScopeDeptIds(ListUtil.empty());
            return;
        }

        // 获取用户的所有角色
        var userRoles = sysUserService.getRolesByUserId(userId);
        if (userRoles == null || userRoles.isEmpty()) {
            userPrincipal.setDataScope(4);
            userPrincipal.setDataScopeDeptIds(ListUtil.empty());
            return;
        }

        // 计算数据权限范围（取最小权限）
        // 优先级：仅本人(4) > 本部门(2) > 本部门及子部门(3) > 自定义(5) > 全部(1)
        Integer finalDataScope = null;
        List<String> finalDeptIds = ListUtil.empty();

        for (var role : userRoles) {
            Integer roleDataScope = role.getDataScope();
            if (roleDataScope == null) {
                continue;
            }

            // 如果是第一个角色，直接设置
            if (finalDataScope == null) {
                finalDataScope = roleDataScope;
                if (roleDataScope == 5) {
                    // 自定义数据权限，获取部门ID列表
                    finalDeptIds = sysUserService.getRoleDeptIds(role.getId());
                }
                continue;
            }

            // 比较权限大小，取更严格的权限
            if (isMoreStrictPermission(roleDataScope, finalDataScope)) {
                finalDataScope = roleDataScope;
                if (roleDataScope == 5) {
                    finalDeptIds = sysUserService.getRoleDeptIds(role.getId());
                }
            }
        }

        // 如果没有找到数据权限，使用默认值
        if (finalDataScope == null) {
            finalDataScope = 4; // 仅本人数据
        }

        userPrincipal.setDataScope(finalDataScope);
        userPrincipal.setDataScopeDeptIds(finalDeptIds);

        log.debug(
            "用户数据权限 - userId: {}, dataScope: {}, deptIds: {}",
            userId,
            finalDataScope,
            finalDeptIds
        );
    }

    /**
     * 判断权限A是否比权限B更严格
     *
     * @param scopeA 权限A
     * @param scopeB 权限B
     * @return true-A更严格，false-A不比B更严格
     */
    private boolean isMoreStrictPermission(Integer scopeA, Integer scopeB) {
        // 优先级从高到低：4(仅本人) > 2(本部门) > 3(本部门及子部门) > 5(自定义) > 1(全部)
        Integer[] priority = { 4, 2, 3, 5, 1 };
        int priorityA = -1,
            priorityB = -1;

        for (int i = 0; i < priority.length; i++) {
            if (priority[i].equals(scopeA)) {
                priorityA = i;
            }
            if (priority[i].equals(scopeB)) {
                priorityB = i;
            }
        }

        return priorityA < priorityB;
    }

    /**
     * 根据用户ID加载用户信息
     *
     * @param userId 用户ID
     * @return 用户详情
     */
    public UserDetails loadUserByUserId(String userId) {
        log.debug("根据用户ID加载用户信息: {}", userId);

        SysUser user = sysUserService.getUserById(userId);
        if (user == null) {
            log.warn("用户不存在: {}", userId);
            throw new UsernameNotFoundException("用户不存在: " + userId);
        }

        return loadUserByUsername(user.getUsername());
    }
}
