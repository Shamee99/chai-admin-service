package org.shamee.system.service.impl;

import lombok.RequiredArgsConstructor;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.date.DateTime;
import org.dromara.hutool.core.date.DateUtil;
import org.shamee.common.constant.CommonConstant;
import org.shamee.common.dto.req.PageRequest;
import org.shamee.common.dto.resp.PageResult;
import org.shamee.common.util.RedisUtil;
import org.shamee.system.dto.req.onlineuser.OnlineUserQueryRequest;
import org.shamee.system.dto.req.user.SysUserQueryRequest;
import org.shamee.system.dto.resp.onlineuser.OnlineUserPageResp;
import org.shamee.system.dto.resp.onlineuser.OnlineUserStatisticResp;
import org.shamee.system.dto.resp.user.SysUserQueryResp;
import org.shamee.system.entity.SysOperationLog;
import org.shamee.system.mapper.SysUserMapper;
import org.shamee.system.service.OnlineUserService;
import org.shamee.system.service.SysOperationLogService;
import org.shamee.system.service.SysUserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * 在线用户监控服务实现类
 *
 * @author shamee
 */
@Service
@RequiredArgsConstructor
public class OnlineUserServiceImpl implements OnlineUserService {

    private final RedisUtil redisUtil;
    private final SysUserMapper sysUserMapper;
    private final SysOperationLogService sysOperationLogService;

    @Override
    public OnlineUserStatisticResp getOnlineUserStats() {
        OnlineUserStatisticResp.OnlineUserStatisticRespBuilder respBuilder = OnlineUserStatisticResp.builder();
        // 总用户数
        Set<String> keys = redisUtil.keys(CommonConstant.RedisKey.USER_INFO + "*");
        long total = keys.size();
        respBuilder.total(total);

        // 活跃用户数， 5分钟内有操作过系统的，且还在线的用户数
        List<String> userIds = keys.stream()
                .map(key -> key.replace(CommonConstant.RedisKey.USER_INFO, "")).toList();
        //
        DateTime fiveMin = DateUtil.offsetMinute(DateUtil.now(), -5);
        Long activeCnt = sysOperationLogService.lambdaQuery()
                .in(SysOperationLog::getUserId, userIds)
                .ge(SysOperationLog::getOperTime, fiveMin).count();
        respBuilder.active(activeCnt);


        // 空闲用户数
        respBuilder.idle(total - activeCnt);

        // 平均会话时长
        respBuilder.duration(0L);
        return respBuilder.build();

    }

    @Override
    public PageResult<OnlineUserPageResp> getOnlineUserList(PageRequest<OnlineUserQueryRequest> request) {
        Set<String> keys = redisUtil.keys(CommonConstant.RedisKey.USER_INFO + "*");

        if (CollUtil.isEmpty(keys)) return PageResult.empty(OnlineUserPageResp.class);

        List<String> userIds = keys.stream()
                .map(key -> key.replace(CommonConstant.RedisKey.USER_INFO, "")).toList();

        if (CollUtil.isEmpty(userIds)) return PageResult.empty(OnlineUserPageResp.class);
        if(request.getParam() != null) {
            request.getParam().setUserIds(userIds);
        } else {
            request.setParam(OnlineUserQueryRequest.builder().userIds(userIds).build());
        }

        return PageResult.of(sysUserMapper.selectOnlineUserList(request.page(), request.getParam()), OnlineUserPageResp::new);
    }


}