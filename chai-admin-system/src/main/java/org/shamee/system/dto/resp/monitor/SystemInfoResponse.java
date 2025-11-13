package org.shamee.system.dto.resp.monitor;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 系统信息响应DTO
 *
 * @author shamee
 * @since 2024-01-15
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SystemInfoResponse {
    
    /**
     * 操作系统
     */
    private String os;
    
    /**
     * 内核版本
     */
    private String kernel;
    
    /**
     * 系统架构
     */
    private String arch;
    
    /**
     * 系统运行时间（秒）
     */
    private Long uptime;
    
    /**
     * CPU信息
     */
    private CpuInfo cpu;
    
    /**
     * 网络接口信息
     */
    private List<NetworkInterfaceInfo> network;
    
    /**
     * CPU信息
     */
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CpuInfo {
        private String model;
        private Integer cores;
        private Double frequency;
        private String cache;
        private List<String> features;
        private Double temperature;
    }
    
    /**
     * 网络接口信息
     */
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NetworkInterfaceInfo {
        private String name;
        private String ip;
        private String mac;
        private String type;
        private Long speed;
        private String status;
    }
}