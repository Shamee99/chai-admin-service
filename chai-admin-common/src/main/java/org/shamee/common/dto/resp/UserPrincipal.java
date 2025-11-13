package org.shamee.common.dto.resp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户认证信息
 *
 * @author shamee
 * @since 2024-01-01
 */
@Data
public class UserPrincipal implements UserDetails {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    @Getter(AccessLevel.NONE)
    private String password;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 部门ID
     */
    private String deptId;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 状态（1启用 0禁用）
     */
    private Integer status;

    /**
     * 权限列表
     */
    private List<String> permissions;

    /**
     * 角色列表
     */
    private List<String> roles;

    /**
     * 数据权限范围
     */
    private Integer dataScope;

    /**
     * 数据权限部门ID列表
     */
    private List<String> dataScopeDeptIds;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return permissions.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return status != null && status == 1;
    }

    /**
     * 获取权限字符串（用于JWT）
     *
     * @return 权限字符串
     */
    public String getAuthoritiesString() {
        if (permissions == null || permissions.isEmpty()) {
            return "";
        }
        return String.join(",", permissions);
    }

    /**
     * 获取角色字符串
     *
     * @return 角色字符串
     */
    public String getRolesString() {
        if (roles == null || roles.isEmpty()) {
            return "";
        }
        return String.join(",", roles);
    }

    /**
     * 是否为超级管理员
     *
     * @return 是否为超级管理员
     */
    public boolean isSuperAdmin() {
        return roles != null && roles.contains("super_admin");
    }
}