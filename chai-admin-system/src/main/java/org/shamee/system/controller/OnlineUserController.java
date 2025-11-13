package org.shamee.system.controller;

import lombok.RequiredArgsConstructor;
import org.shamee.common.dto.req.PageRequest;
import org.shamee.common.dto.resp.PageResult;
import org.shamee.common.dto.resp.R;
import org.shamee.system.dto.req.onlineuser.OnlineUserQueryRequest;
import org.shamee.system.dto.req.user.SysUserQueryRequest;
import org.shamee.system.dto.resp.onlineuser.OnlineUserPageResp;
import org.shamee.system.dto.resp.onlineuser.OnlineUserStatisticResp;
import org.shamee.system.dto.resp.user.SysUserQueryResp;
import org.shamee.system.service.OnlineUserService;
import org.springframework.web.bind.annotation.*;

/**
 * 在线用户监控控制器
 *
 * @author shamee
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/monitor/online-user")
public class OnlineUserController {

    private final OnlineUserService onlineUserService;

    /**
     * 获取在线用户统计
     */
    @GetMapping("/stats")
    public R<OnlineUserStatisticResp> getOnlineUserStats() {
        return R.success(onlineUserService.getOnlineUserStats());
    }

    /**
     * 获取在线用户列表
     */
    @PostMapping("/list")
    public R<PageResult<OnlineUserPageResp>> getOnlineUserList(@RequestBody PageRequest<OnlineUserQueryRequest> pageRequest) {
        return R.success(onlineUserService.getOnlineUserList(pageRequest));
    }


}