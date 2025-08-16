package org.shamee.system.service;

import org.shamee.common.dto.req.PageRequest;
import org.shamee.common.dto.resp.PageResult;
import org.shamee.system.dto.req.onlineuser.OnlineUserQueryRequest;
import org.shamee.system.dto.resp.onlineuser.OnlineUserPageResp;
import org.shamee.system.dto.resp.onlineuser.OnlineUserStatisticResp;

/**
 * 在线用户监控服务接口
 *
 * @author shamee
 */
public interface OnlineUserService {

    /**
     * 获取在线用户统计
     */
    OnlineUserStatisticResp getOnlineUserStats();

    /**
     * 获取在线用户列表
     */
    PageResult<OnlineUserPageResp> getOnlineUserList(PageRequest<OnlineUserQueryRequest> pageRequest);


}