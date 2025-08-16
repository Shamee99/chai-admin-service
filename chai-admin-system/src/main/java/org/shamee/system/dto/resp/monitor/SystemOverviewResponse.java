package org.shamee.system.dto.resp.monitor;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 系统概览响应DTO
 *
 * @author shamee
 * @since 2024-01-15
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SystemOverviewResponse {

    /**
     * 系统状态
     */
    private SystemStatusResponse status;

    /**
     * 系统资源
     */
    private SystemResourceResponse resource;

    /**
     * 服务列表
     */
    private List<ServiceStatusResponse> services;

    /**
     * 告警列表
     */
    private List<SystemAlertResponse> alerts;

    /**
     * 性能历史数据
     */
    private SystemPerformanceHistoryResponse performance;

    /**
     * 服务状态响应DTO
     */
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ServiceStatusResponse {
        /**
         * 服务名称
         */
        private String name;

        /**
         * 服务状态：running、stopped、error
         */
        private String status;

        /**
         * 运行时间
         */
        private String uptime;

        /**
         * 端口
         */
        private Integer port;

        /**
         * 描述
         */
        private String description;
    }
}