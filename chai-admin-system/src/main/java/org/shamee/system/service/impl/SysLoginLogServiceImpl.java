package org.shamee.system.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.shamee.common.dto.req.PageRequest;
import org.shamee.common.dto.resp.PageResult;
import org.shamee.system.dto.req.loginlog.LoginLogQueryRequest;
import org.shamee.system.dto.resp.loginlog.LoginLogPageResp;
import org.shamee.system.entity.SysLoginLog;
import org.shamee.system.mapper.SysLoginLogMapper;
import org.shamee.system.service.SysLoginLogService;
import org.springframework.stereotype.Service;

@Service
public class SysLoginLogServiceImpl extends ServiceImpl<SysLoginLogMapper, SysLoginLog> implements SysLoginLogService {


    @Override
    public PageResult<LoginLogPageResp> getLoginLogList(PageRequest<LoginLogQueryRequest> request) {
        Page<SysLoginLog> page = new Page<>(request.getPageNo(), request.getPageSize());
        Page<SysLoginLog> logPage = this.lambdaQuery()
//                .like(request.getParam() != null && StrUtil.isNotEmpty(request.getParam().getUsername()), SysLoginLog::getUsername, request.getParam().getUsername())
//                .like(request.getParam() != null && StrUtil.isNotEmpty(request.getParam().getIpAddress()), SysLoginLog::getIpaddr, request.getParam().getIpAddress())
                .orderByDesc(SysLoginLog::getLoginTime)
                .page(page);

        return PageResult.of(logPage, LoginLogPageResp::new, (s, t) -> {
            t.setIpaddr(s.getIpaddr());
        });
    }
}
