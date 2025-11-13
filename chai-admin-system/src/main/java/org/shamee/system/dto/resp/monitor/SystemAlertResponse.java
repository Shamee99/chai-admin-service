package org.shamee.system.dto.resp.monitor;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * 系统告警响应DTO
 *
 * @author shamee
 * @since 2024-01-15
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SystemAlertResponse {
    
    /**
     * 告警ID
     */
    private String id;
    
    /**
     * 告警级别：严重、警告、信息
     */
    private String level;
    
    /**
     * 告警消息
     */
    private String message;
    
    /**
     * 告警时间
     */
    private String time;
    
    /**
     * 处理状态：已处理、未处理
     */
    private String status;
}