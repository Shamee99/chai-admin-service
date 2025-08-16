package org.shamee.system.controller;

import lombok.RequiredArgsConstructor;
import org.shamee.common.annotation.Anonymous;
import org.shamee.common.dto.resp.R;
import org.shamee.system.dto.resp.monitor.SystemOverviewResponse;
import org.shamee.system.dto.resp.monitor.SystemStatusResponse;
import org.shamee.system.dto.resp.monitor.SystemAlertResponse;
import org.shamee.system.dto.resp.monitor.SystemPerformanceHistoryResponse;
import org.shamee.system.service.impl.MonitorOverviewServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统概览监控控制器
 *
 * @author shamee
 * @since 2024-01-15
 */
@RestController
@RequestMapping("/api/monitor")
@RequiredArgsConstructor
@Anonymous
public class MonitorOverviewController {

    private final MonitorOverviewServiceImpl monitorOverviewServiceImpl;

    /**
     * 获取系统状态
     */
    @GetMapping("/system/status")
    public R<SystemStatusResponse> getSystemStatus() {
        return R.success(monitorOverviewServiceImpl.getSystemStatus());
    }

    /**
     * 获取系统告警信息
     */
    @GetMapping("/alerts")
    public R<List<SystemAlertResponse>> getSystemAlerts() {
        return R.success(monitorOverviewServiceImpl.getSystemAlerts());
    }

    /**
     * 获取系统概览数据
     */
    @GetMapping("/overview")
    public R<SystemOverviewResponse> getSystemOverview() {
        return R.success(monitorOverviewServiceImpl.getSystemOverview());
    }

    /**
     * 获取系统性能历史数据
     */
    @GetMapping("/performance/history")
    public R<SystemPerformanceHistoryResponse> getSystemPerformanceHistory(
            @RequestParam(defaultValue = "1h") String range) {
        return R.success(monitorOverviewServiceImpl.getSystemPerformanceHistory(range));
    }

    /**
     * 清除告警
     */
    @DeleteMapping("/alerts/{alertId}")
    public R<Void> clearAlert(@PathVariable String alertId) {
        monitorOverviewServiceImpl.clearAlert(alertId);
        return R.success();
    }

    /**
     * 批量清除告警
     */
    @PostMapping("/alerts/clear")
    public R<Void> clearAlerts(@RequestBody List<String> alertIds) {
        monitorOverviewServiceImpl.clearAlerts(alertIds);
        return R.success();
    }

    /**
     * 获取实时系统资源数据
     */
    @GetMapping("/system/resource/realtime")
    public R<SystemOverviewResponse> getRealTimeSystemResource() {
        return R.success(monitorOverviewServiceImpl.getRealTimeSystemResource());
    }
}