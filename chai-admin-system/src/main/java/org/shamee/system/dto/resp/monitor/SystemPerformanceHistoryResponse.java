package org.shamee.system.dto.resp.monitor;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 系统性能历史数据响应DTO
 *
 * @author shamee
 * @since 2024-01-15
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SystemPerformanceHistoryResponse {

    /**
     * CPU历史数据
     */
    private List<ChartDataPoint> cpu;

    /**
     * 内存历史数据
     */
    private List<ChartDataPoint> memory;

    /**
     * 磁盘历史数据
     */
    private List<ChartDataPoint> disk;

    /**
     * 网络历史数据
     */
    private List<ChartDataPoint> network;

    /**
     * 图表数据点
     */
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ChartDataPoint {
        /**
         * 时间
         */
        private String time;

        /**
         * 数值
         */
        private Double value;
    }
}