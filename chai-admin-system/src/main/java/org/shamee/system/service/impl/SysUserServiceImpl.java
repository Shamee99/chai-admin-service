package org.shamee.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.data.id.IdUtil;
import org.shamee.common.constant.CommonConstant;
import org.shamee.common.dto.req.PageRequest;
import org.shamee.common.dto.resp.PageResult;
import org.shamee.common.entity.IdEntity;
import org.shamee.common.util.PasswordUtil;
import org.shamee.common.util.copy.BeanCopierUtils;
import org.shamee.system.dto.req.user.SysUserEditRequest;
import org.shamee.system.dto.req.user.SysUserQueryRequest;
import org.shamee.system.dto.req.user.SysUserSaveRequest;
import org.shamee.system.dto.resp.user.SysUserDetailResp;
import org.shamee.system.dto.resp.user.SysUserQueryResp;
import org.shamee.system.entity.SysMenu;
import org.shamee.system.entity.SysRole;
import org.shamee.system.entity.SysUser;
import org.shamee.system.entity.SysUserRole;
import org.shamee.system.mapper.SysUserMapper;
import org.shamee.system.mapper.SysUserRoleMapper;
import org.shamee.system.service.SysMenuService;
import org.shamee.system.service.SysRoleService;
import org.shamee.system.service.SysUserRoleService;
import org.shamee.system.service.SysUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 系统用户服务实现类
 *
 * @author shamee
 * @since 2024-01-01
 */
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    private final SysUserRoleMapper sysUserRoleMapper;
    private final SysMenuService sysMenuService;
    private final SysUserRoleService sysUserRoleService;
    private final SysRoleService sysRoleService;

    @Override
    public PageResult<SysUserQueryResp> getUserPage(PageRequest<SysUserQueryRequest> request) {
        Page<SysUserQueryResp> deptPageResps = baseMapper.selectUserList(request.page(), request.getParam());
        return PageResult.of(deptPageResps, SysUserQueryResp::new);
    }

    @Override
    public SysUser getUserById(String userId) {
        return this.getOptById(userId).orElse(null);
    }

    @Override
    public SysUser getUserByUsername(String username) {
        Optional<SysUser> sysUserOpt = this.lambdaQuery().eq(SysUser::getUsername, username).oneOpt();
        return sysUserOpt.orElse(null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveUser(SysUserSaveRequest request) {
        SysUser user = BeanCopierUtils.copy(request, SysUser::new);
        // 检查用户名是否重复
        if (checkUsernameExists(request.getUsername(), null)) {
            throw new RuntimeException("用户名已存在");
        }

        // 设置默认密码
        user.setId(IdUtil.getSeataSnowflakeNextIdStr());
        String password = StringUtils.hasText(request.getPassword()) ?
                request.getPassword() : CommonConstant.DEFAULT_PASSWORD;
        String[] saltAndPassword = PasswordUtil.generateSaltAndEncodePassword(password);
        user.setSalt(saltAndPassword[0]);
        user.setPassword(saltAndPassword[1]);
        boolean saveFlag = this.save(user);

        // 添加角色
        if(saveFlag && CollUtil.isNotEmpty(request.getRoleIds())) {
            List<SysUserRole> userRoles = new ArrayList<>();
            request.getRoleIds().forEach(roleId -> {
                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setUserId(user.getId());
                sysUserRole.setRoleId(roleId);
                userRoles.add(sysUserRole);
            });
            sysUserRoleService.saveBatch(userRoles);
        }
        return true;
    }

    @Override
    public SysUserDetailResp detail(String id) {
        SysUser sysUser = this.getById(id);
        Objects.requireNonNull(sysUser, "该用户不存在，请确认");

        return BeanCopierUtils.copy(sysUser, SysUserDetailResp::new, (s, t) -> {
            List<SysRole> sysRoles = sysRoleService.getRolesByUserId(s.getId());
            if(CollUtil.isNotEmpty(sysRoles)) {
                t.setRoleIds(sysRoles.stream().map(IdEntity::getId).toList());
            }
        });
    }

    @Override
    public boolean editUser(SysUserEditRequest request) {
        // 更新用户
        SysUser existUser = getById(request.getId());
        if (existUser == null) {
            throw new RuntimeException("用户不存在");
        }

        // 检查邮箱是否重复
        if (StringUtils.hasText(request.getEmail()) &&
                checkEmailExists(request.getEmail(), request.getId())) {
            throw new RuntimeException("邮箱已存在");
        }

        // 检查手机号是否重复
        if (StringUtils.hasText(request.getPhone()) &&
                checkPhoneExists(request.getPhone(), request.getId())) {
            throw new RuntimeException("手机号已存在");
        }

        SysUser editUser = BeanCopierUtils.copy(request, SysUser::new);
        boolean updateFlag = this.updateById(editUser);

        // 删除原有的关联角色，新增新的角色
        sysUserRoleService.remove(
                Wrappers.lambdaQuery(SysUserRole.class).eq(SysUserRole::getUserId, request.getId())
        );

        if(updateFlag && CollUtil.isNotEmpty(request.getRoleIds())) {
            List<SysUserRole> userRoles = new ArrayList<>();
            request.getRoleIds().forEach(roleId -> {
                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setUserId(request.getId());
                sysUserRole.setRoleId(roleId);
                userRoles.add(sysUserRole);
            });
            sysUserRoleService.saveBatch(userRoles);
        }

        return true;
    }


    @Override
    public boolean updateUserStatus(String userId, Integer status) {
        SysUser user = new SysUser();
        user.setId(userId);
        user.setStatus(status);
        return updateById(user);
    }

    @Override
    public boolean resetUserPassword(String userId) {
        SysUser user = new SysUser();
        user.setId(userId);
        String[] saltAndPassword = PasswordUtil.generateSaltAndEncodePassword(CommonConstant.DEFAULT_PASSWORD);
        user.setSalt(saltAndPassword[0]);
        user.setPassword(saltAndPassword[1]);
        return updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteUser(String userId) {
        // 删除用户角色关联
        sysUserRoleService.remove(
                Wrappers.lambdaQuery(SysUserRole.class).eq(SysUserRole::getUserId, userId)
        );

        // 删除用户
        return removeById(userId);
    }

    @Override
    public boolean checkUsernameExists(String username, String excludeId) {
        return this.count(new QueryWrapper<SysUser>().lambda()
                .eq(SysUser::getUsername, username)
                .ne(excludeId != null, SysUser::getId, excludeId)
        ) > 0;
    }

    @Override
    public boolean checkEmailExists(String email, String excludeId) {
        return this.count(
                Wrappers.lambdaQuery(SysUser.class).eq(SysUser::getEmail, email)
                        .ne(excludeId != null, SysUser::getId, excludeId)
        ) > 0;
    }

    @Override
    public boolean checkPhoneExists(String phone, String excludeId) {
        return this.count(
                Wrappers.lambdaQuery(SysUser.class).eq(SysUser::getPhone, phone)
                        .ne(excludeId != null, SysUser::getId, excludeId)
        ) > 0;
    }

    @Override
    public List<String> getUserPermissions(String userId) {
        // 查询当前角色，如果是SUPER_ADMIN，则赋予所有权限
        SysUser sysUser = this.getById(userId);
        Objects.requireNonNull(sysUser, "该用户不存在，请确认");

        if(sysUser.getUsername().equalsIgnoreCase(CommonConstant.SUPER_ADMIN)) {
            return sysMenuService.list(Wrappers.lambdaQuery(SysMenu.class).select(SysMenu::getId))
                    .stream().map(IdEntity::getId).collect(Collectors.toList());
        }
        return sysMenuService.getUserPermissions(userId);
    }

    @Override
    public void updateLoginInfo(String userId, String ip) {
        SysUser user = new SysUser();
        user.setId(userId);
        user.setLastLoginTime(LocalDateTime.now());
        user.setLastLoginIp(ip);
        updateById(user);
    }
}