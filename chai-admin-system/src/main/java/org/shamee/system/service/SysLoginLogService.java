package org.shamee.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.shamee.common.dto.req.PageRequest;
import org.shamee.common.dto.resp.PageResult;
import org.shamee.system.dto.req.loginlog.LoginLogQueryRequest;
import org.shamee.system.dto.resp.loginlog.LoginLogPageResp;
import org.shamee.system.entity.SysLoginLog;

public interface SysLoginLogService extends IService<SysLoginLog> {

    /**
     * 获取登录日志列表
     */
    PageResult<LoginLogPageResp> getLoginLogList(PageRequest<LoginLogQueryRequest> request);
}
