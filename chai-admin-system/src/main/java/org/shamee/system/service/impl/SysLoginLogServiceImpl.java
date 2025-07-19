package org.shamee.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.shamee.system.entity.SysLoginLog;
import org.shamee.system.mapper.SysLoginLogMapper;
import org.shamee.system.service.SysLoginLogService;
import org.springframework.stereotype.Service;

@Service
public class SysLoginLogServiceImpl extends ServiceImpl<SysLoginLogMapper, SysLoginLog> implements SysLoginLogService {
}
