package org.shamee.system.service;

import org.shamee.system.dto.resp.monitor.SystemAlertResponse;
import org.shamee.system.dto.resp.monitor.SystemOverviewResponse;
import org.shamee.system.dto.resp.monitor.SystemPerformanceHistoryResponse;
import org.shamee.system.dto.resp.monitor.SystemStatusResponse;

import java.util.List;

public interface IMonitorOverviewService {

    SystemStatusResponse getSystemStatus();
    List<SystemAlertResponse> getSystemAlerts();

    SystemOverviewResponse getSystemOverview();

    SystemPerformanceHistoryResponse getSystemPerformanceHistory(String range);

    void clearAlert(String alertId);

    void clearAlerts(List<String> alertIds);

    SystemOverviewResponse getRealTimeSystemResource();
}
