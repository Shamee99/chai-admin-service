package org.shamee.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.shamee.system.entity.SysUserRole;
import org.shamee.system.mapper.SysUserRoleMapper;
import org.shamee.system.service.SysUserRoleService;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService {



}
