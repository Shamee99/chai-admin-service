package org.shamee.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.shamee.system.entity.SysRoleMenu;

public interface SysRoleMenuService extends IService<SysRoleMenu> {


    void deleteRoleMenuByRoleId(String roleId);
}
