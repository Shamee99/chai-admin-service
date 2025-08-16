package org.shamee.system.dto.resp.monitor;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * 系统资源响应DTO
 *
 * @author shamee
 * @since 2024-01-15
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SystemResourceResponse {
    
    /**
     * CPU信息
     */
    private CpuInfo cpu;
    
    /**
     * 内存信息
     */
    private MemoryInfo memory;
    
    /**
     * 磁盘信息
     */
    private DiskInfo disk;
    
    /**
     * 网络信息
     */
    private NetworkInfo network;
    
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CpuInfo {
        private Double usage;
        private Integer cores;
        private Integer processes;
    }
    
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MemoryInfo {
        private Double usage;
        private Long total;
        private Long used;
        private Long free;
    }
    
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DiskInfo {
        private Double usage;
        private Long total;
        private Long used;
        private Long free;
    }
    
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NetworkInfo {
        private Long inbound;
        private Long outbound;
        private Integer connections;
    }
}