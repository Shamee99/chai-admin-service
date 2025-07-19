package org.shamee.system.security;

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

import java.util.List;

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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
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
        List<String> permissions = sysUserService.getUserPermissions(user.getId());
        
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
        List<String> roles = permissions.stream()
                .filter(permission -> permission.startsWith("ROLE_"))
                .toList();
        userPrincipal.setRoles(roles);
        
        // 设置数据权限（这里简化处理，实际应该根据角色查询）
        userPrincipal.setDataScope(1); // 默认全部数据权限
        userPrincipal.setDataScopeDeptIds(ListUtil.empty());
        
        log.debug("用户信息加载完成: {}, 权限数量: {}", username, permissions.size());
        return userPrincipal;
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