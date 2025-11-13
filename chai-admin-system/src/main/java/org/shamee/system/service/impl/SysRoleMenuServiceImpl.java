package org.shamee.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shamee.system.entity.SysRoleMenu;
import org.shamee.system.mapper.SysRoleMenuMapper;
import org.shamee.system.service.SysRoleMenuService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements SysRoleMenuService {


    @Override
    public void deleteRoleMenuByRoleId(String roleId) {
        this.baseMapper.deleteRoleMenuByRoleId(roleId);
    }

}
