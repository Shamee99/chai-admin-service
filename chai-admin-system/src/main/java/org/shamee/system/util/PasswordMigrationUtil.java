package org.shamee.system.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shamee.common.util.PasswordUtil;
import org.shamee.system.entity.SysUser;
import org.shamee.system.service.SysUserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 密码迁移工具类
 * 用于为现有用户添加盐值并重新加密密码
 *
 * @author shamee
 * @since 2024-01-01
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PasswordMigrationUtil implements CommandLineRunner {

    private final SysUserService sysUserService;

    @Override
    public void run(String... args) throws Exception {
        // 检查是否需要执行密码迁移
        if (needPasswordMigration()) {
            log.info("开始执行密码迁移...");
            migratePasswords();
            log.info("密码迁移完成");
        } else {
            log.debug("无需执行密码迁移");
        }
    }

    /**
     * 检查是否需要执行密码迁移
     * 如果存在用户的salt字段为空，则需要迁移
     */
    private boolean needPasswordMigration() {
        try {
            List<SysUser> users = sysUserService.list();
            return users.stream().anyMatch(user -> !StringUtils.hasText(user.getSalt()));
        } catch (Exception e) {
            log.warn("检查密码迁移状态时发生异常，可能是salt字段不存在: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 执行密码迁移
     * 为所有没有盐值的用户生成盐值并重新加密密码
     */
    private void migratePasswords() {
        try {
            List<SysUser> users = sysUserService.list();
            
            for (SysUser user : users) {
                if (!StringUtils.hasText(user.getSalt())) {
                    // 为没有盐值的用户重置密码为默认密码123456
                    String defaultPassword = "123456";
                    String[] saltAndPassword = PasswordUtil.generateSaltAndEncodePassword(defaultPassword);
                    
                    user.setSalt(saltAndPassword[0]);
                    user.setPassword(saltAndPassword[1]);
                    
                    sysUserService.updateById(user);
                    
                    log.info("已为用户 {} 生成盐值并重置密码", user.getUsername());
                }
            }
        } catch (Exception e) {
            log.error("执行密码迁移时发生异常: {}", e.getMessage(), e);
        }
    }
}