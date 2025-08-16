package org.shamee.system.controller;

import lombok.RequiredArgsConstructor;
import org.shamee.common.dto.req.PageRequest;
import org.shamee.common.dto.resp.PageResult;
import org.shamee.common.dto.resp.R;
import org.shamee.system.dto.req.loginlog.LoginLogQueryRequest;
import org.shamee.system.dto.resp.loginlog.LoginLogPageResp;
import org.shamee.system.service.SysLoginLogService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录日志监控控制器
 * 
 * @author shamee
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/monitor/login-logs")
public class LoginLogController {

    private final SysLoginLogService loginLogService;

    /**
     * 获取登录日志列表
     */
    @PostMapping
    public R<PageResult<LoginLogPageResp>> getLoginLogList(@RequestBody PageRequest<LoginLogQueryRequest> request) {
        return R.success(loginLogService.getLoginLogList(request));
    }

}