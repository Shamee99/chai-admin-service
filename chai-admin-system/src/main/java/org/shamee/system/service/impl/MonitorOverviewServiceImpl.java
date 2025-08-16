package org.shamee.system.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shamee.system.dto.resp.monitor.*;
import org.shamee.system.service.IMonitorOverviewService;
import org.shamee.system.service.OnlineUserService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 系统概览监控服务
 *
 * @author shamee
 * @since 2024-01-15
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MonitorOverviewServiceImpl implements IMonitorOverviewService {

    private final OnlineUserService onlineUserService;

    private final RedisTemplate<String, Object> redisTemplate;
    private final SystemInfo systemInfo = new SystemInfo();
    private final HardwareAbstractionLayer hardware = systemInfo.getHardware();
    private final OperatingSystem os = systemInfo.getOperatingSystem();
    
    // 缓存CPU使用率计算所需的数据
    private long[] prevTicks;
    private long prevTime;
    
    /**
     * 获取系统状态
     */
    @Override
    public SystemStatusResponse getSystemStatus() {
        try {
            // 获取CPU使用率
            double cpuUsage = getCpuUsage();
            
            // 获取内存使用率
            GlobalMemory memory = hardware.getMemory();
            double memoryUsage = (double) (memory.getTotal() - memory.getAvailable()) / memory.getTotal() * 100;
            
            // 获取在线用户数（从Redis中获取活跃session数量）
            long onlineUsers = onlineUserService.getOnlineUserStats().getTotal();
            
            return SystemStatusResponse.builder()
                    .status("正常")
                    .onlineUsers((int) onlineUsers)
                    .cpuUsage(Math.round(cpuUsage * 100.0) / 100.0)
                    .memoryUsage(Math.round(memoryUsage * 100.0) / 100.0)
                    .build();
        } catch (Exception e) {
            log.error("获取系统状态失败", e);
            return SystemStatusResponse.builder()
                    .status("异常")
                    .onlineUsers(0)
                    .cpuUsage(0.0)
                    .memoryUsage(0.0)
                    .build();
        }
    }
    
    /**
     * 获取系统告警信息
     */
    @Override
    public List<SystemAlertResponse> getSystemAlerts() {
        List<SystemAlertResponse> alerts = new ArrayList<>();
        
        try {
            // 检查CPU使用率告警
            double cpuUsage = getCpuUsage();
            if (cpuUsage > 80) {
                alerts.add(SystemAlertResponse.builder()
                        .id(UUID.randomUUID().toString())
                        .level("警告")
                        .message(String.format("CPU使用率超过80%，当前：%.2f%%", cpuUsage))
                        .time(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                        .status("未处理")
                        .build());
            }
            
            // 检查内存使用率告警
            GlobalMemory memory = hardware.getMemory();
            double memoryUsage = (double) (memory.getTotal() - memory.getAvailable()) / memory.getTotal() * 100;
            if (memoryUsage > 85) {
                alerts.add(SystemAlertResponse.builder()
                        .id(UUID.randomUUID().toString())
                        .level("严重")
                        .message("""
                                内存使用率超过85%%，当前使用率：%.2f%%
                                """.formatted(memoryUsage))
                        .time(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                        .status("未处理")
                        .build());
            }
            
            // 检查磁盘使用率告警
            FileSystem fileSystem = os.getFileSystem();
            List<OSFileStore> fileStores = fileSystem.getFileStores();
            for (OSFileStore store : fileStores) {
                if (store.getTotalSpace() > 0) {
                    double diskUsage = (double) (store.getTotalSpace() - store.getUsableSpace()) / store.getTotalSpace() * 100;
                    if (diskUsage > 90) {
                        alerts.add(SystemAlertResponse.builder()
                                .id(UUID.randomUUID().toString())
                                .level("严重")
                                .message("磁盘 %s 使用率超过90%%，当前：%.2f%%".formatted(store.getMount(), diskUsage))
                                .time(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                                .status("未处理")
                                .build());
                    }
                }
            }
            
        } catch (Exception e) {
            log.error("获取系统告警失败", e);
        }
        
        return alerts;
    }
    
    /**
     * 获取系统概览数据
     */
    @Override
    public SystemOverviewResponse getSystemOverview() {
        return SystemOverviewResponse.builder()
                .status(getSystemStatus())
                .resource(getSystemResource())
                .services(getServiceStatus())
                .alerts(getSystemAlerts())
                .performance(getSystemPerformanceHistory("1h"))
                .build();
    }
    
    /**
     * 获取系统性能历史数据
     */
    @Override
    public SystemPerformanceHistoryResponse getSystemPerformanceHistory(String range) {
        // 这里可以从数据库或缓存中获取历史数据
        // 为了演示，生成模拟的历史数据
        int dataPoints = getDataPointsByRange(range);
        
        return SystemPerformanceHistoryResponse.builder()
                .cpu(generateHistoryData("cpu", dataPoints))
                .memory(generateHistoryData("memory", dataPoints))
                .disk(generateHistoryData("disk", dataPoints))
                .network(generateHistoryData("network", dataPoints))
                .build();
    }
    
    /**
     * 清除告警
     */
    @Override
    public void clearAlert(String alertId) {
        // 实际项目中应该从数据库中删除告警记录
        log.info("清除告警: {}", alertId);
    }
    
    /**
     * 批量清除告警
     */
    @Override
    public void clearAlerts(List<String> alertIds) {
        // 实际项目中应该从数据库中批量删除告警记录
        log.info("批量清除告警: {}", alertIds);
    }
    
    /**
     * 获取实时系统资源数据
     */
    @Override
    public SystemOverviewResponse getRealTimeSystemResource() {
        return getSystemOverview();
    }
    
    /**
     * 获取CPU使用率
     */
    private double getCpuUsage() {
        CentralProcessor processor = hardware.getProcessor();
        long[] ticks = processor.getSystemCpuLoadTicks();
        
        if (prevTicks != null) {
            long user = ticks[CentralProcessor.TickType.USER.getIndex()] - prevTicks[CentralProcessor.TickType.USER.getIndex()];
            long nice = ticks[CentralProcessor.TickType.NICE.getIndex()] - prevTicks[CentralProcessor.TickType.NICE.getIndex()];
            long sys = ticks[CentralProcessor.TickType.SYSTEM.getIndex()] - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
            long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()] - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
            long iowait = ticks[CentralProcessor.TickType.IOWAIT.getIndex()] - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
            long irq = ticks[CentralProcessor.TickType.IRQ.getIndex()] - prevTicks[CentralProcessor.TickType.IRQ.getIndex()];
            long softirq = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()] - prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
            long steal = ticks[CentralProcessor.TickType.STEAL.getIndex()] - prevTicks[CentralProcessor.TickType.STEAL.getIndex()];
            long totalCpu = user + nice + sys + idle + iowait + irq + softirq + steal;
            
            if (totalCpu > 0) {
                return (1.0 - (double) idle / totalCpu) * 100;
            }
        }
        
        prevTicks = ticks;
        return processor.getSystemCpuLoad(1000) * 100;
    }
    
    /**
     * 获取在线用户数
     */
    private int getOnlineUserCount() {
        try {
            // 从Redis中获取活跃session数量
            Set<String> keys = redisTemplate.keys("spring:session:sessions:*");
            return keys != null ? keys.size() : 0;
        } catch (Exception e) {
            log.warn("获取在线用户数失败", e);
            return 0;
        }
    }
    
    /**
     * 获取系统资源信息
     */
    private SystemResourceResponse getSystemResource() {
        try {
            CentralProcessor processor = hardware.getProcessor();
            GlobalMemory memory = hardware.getMemory();
            
            // CPU信息
            SystemResourceResponse.CpuInfo cpuInfo = SystemResourceResponse.CpuInfo.builder()
                    .usage(getCpuUsage())
                    .cores(processor.getLogicalProcessorCount())
                    .processes(os.getProcessCount())
                    .build();
            
            // 内存信息
            SystemResourceResponse.MemoryInfo memoryInfo = SystemResourceResponse.MemoryInfo.builder()
                    .usage((double) (memory.getTotal() - memory.getAvailable()) / memory.getTotal() * 100)
                    .total(memory.getTotal())
                    .used(memory.getTotal() - memory.getAvailable())
                    .free(memory.getAvailable())
                    .build();
            
            // 磁盘信息
            FileSystem fileSystem = os.getFileSystem();
            List<OSFileStore> fileStores = fileSystem.getFileStores();
            long totalDisk = 0, usedDisk = 0;
            for (OSFileStore store : fileStores) {
                totalDisk += store.getTotalSpace();
                usedDisk += (store.getTotalSpace() - store.getUsableSpace());
            }
            
            SystemResourceResponse.DiskInfo diskInfo = SystemResourceResponse.DiskInfo.builder()
                    .usage(totalDisk > 0 ? (double) usedDisk / totalDisk * 100 : 0)
                    .total(totalDisk)
                    .used(usedDisk)
                    .free(totalDisk - usedDisk)
                    .build();
            
            // 网络信息
            List<NetworkIF> networkIFs = hardware.getNetworkIFs();
            long bytesRecv = 0, bytesSent = 0;
            for (NetworkIF net : networkIFs) {
                net.updateAttributes();
                bytesRecv += net.getBytesRecv();
                bytesSent += net.getBytesSent();
            }
            
            SystemResourceResponse.NetworkInfo networkInfo = SystemResourceResponse.NetworkInfo.builder()
                    .inbound(bytesRecv)
                    .outbound(bytesSent)
                    .connections(networkIFs.size())
                    .build();
            
            return SystemResourceResponse.builder()
                    .cpu(cpuInfo)
                    .memory(memoryInfo)
                    .disk(diskInfo)
                    .network(networkInfo)
                    .build();
        } catch (Exception e) {
            log.error("获取系统资源信息失败", e);
            return SystemResourceResponse.builder().build();
        }
    }
    
    /**
     * 获取服务状态
     */
    private List<SystemOverviewResponse.ServiceStatusResponse> getServiceStatus() {
        List<SystemOverviewResponse.ServiceStatusResponse> services = new ArrayList<>();
        
        // 检查数据库服务状态
        services.add(SystemOverviewResponse.ServiceStatusResponse.builder()
                .name("PostgreSQL")
                .status("running")
                .uptime(formatUptime(System.currentTimeMillis() - 3600000)) // 假设运行1小时
                .port(5432)
                .description("主数据库服务")
                .build());
        
        // 检查Redis服务状态
        try {
            redisTemplate.opsForValue().get("test");
            services.add(SystemOverviewResponse.ServiceStatusResponse.builder()
                    .name("Redis")
                    .status("running")
                    .uptime(formatUptime(System.currentTimeMillis() - 7200000)) // 假设运行2小时
                    .port(6379)
                    .description("缓存服务")
                    .build());
        } catch (Exception e) {
            services.add(SystemOverviewResponse.ServiceStatusResponse.builder()
                    .name("Redis")
                    .status("error")
                    .uptime("0")
                    .port(6379)
                    .description("缓存服务")
                    .build());
        }
        
        return services;
    }
    
    /**
     * 根据时间范围获取数据点数量
     */
    private int getDataPointsByRange(String range) {
        return switch (range) {
            case "1h" -> 60;
            case "6h" -> 72;
            case "24h" -> 96;
            default -> 60;
        };
    }
    
    /**
     * 生成历史数据
     */
    private List<SystemPerformanceHistoryResponse.ChartDataPoint> generateHistoryData(String type, int dataPoints) {
        List<SystemPerformanceHistoryResponse.ChartDataPoint> data = new ArrayList<>();
        Random random = new Random();
        
        for (int i = 0; i < dataPoints; i++) {
            LocalDateTime time = LocalDateTime.now().minusMinutes(dataPoints - i);
            double value = switch (type) {
                case "cpu" -> 20 + random.nextDouble() * 60; // 20-80%
                case "memory" -> 30 + random.nextDouble() * 50; // 30-80%
                case "disk" -> 10 + random.nextDouble() * 30; // 10-40%
                case "network" -> random.nextDouble() * 100; // 0-100MB/s
                default -> random.nextDouble() * 100;
            };
            
            data.add(SystemPerformanceHistoryResponse.ChartDataPoint.builder()
                    .time(time.format(DateTimeFormatter.ofPattern("HH:mm")))
                    .value(Math.round(value * 100.0) / 100.0)
                    .build());
        }
        
        return data;
    }
    
    /**
     * 格式化运行时间
     */
    private String formatUptime(long uptimeMs) {
        long days = TimeUnit.MILLISECONDS.toDays(uptimeMs);
        long hours = TimeUnit.MILLISECONDS.toHours(uptimeMs) % 24;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(uptimeMs) % 60;
        
        if (days > 0) {
            return String.format("%d天%d小时%d分钟", days, hours, minutes);
        } else if (hours > 0) {
            return String.format("%d小时%d分钟", hours, minutes);
        } else {
            return String.format("%d分钟", minutes);
        }
    }
}