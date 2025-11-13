package org.shamee.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.shamee.system.entity.SysRoleDept;
import org.shamee.system.mapper.SysRoleDeptMapper;
import org.shamee.system.service.SysRoleDeptService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SysRoleDeptServiceImpl extends ServiceImpl<SysRoleDeptMapper, SysRoleDept> implements SysRoleDeptService {
}
