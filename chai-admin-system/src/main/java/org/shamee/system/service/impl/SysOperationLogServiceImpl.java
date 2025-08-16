package org.shamee.system.service.impl;

import com.github.yulichang.base.MPJBaseServiceImpl;
import lombok.RequiredArgsConstructor;
import org.shamee.system.entity.SysOperationLog;
import org.shamee.system.mapper.SysOperationLogMapper;
import org.shamee.system.service.SysOperationLogService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SysOperationLogServiceImpl extends MPJBaseServiceImpl<SysOperationLogMapper, SysOperationLog> implements SysOperationLogService {
}
