package org.shamee.system.controller;

import lombok.RequiredArgsConstructor;
import org.shamee.common.annotation.Anonymous;
import org.shamee.common.dto.req.PageRequest;
import org.shamee.common.dto.resp.PageResult;
import org.shamee.common.dto.resp.R;
import org.shamee.system.dto.req.monitor.CacheQueryRequest;
import org.shamee.system.dto.resp.monitor.CachePageResp;
import org.shamee.system.dto.resp.monitor.CacheStatusResp;
import org.shamee.system.service.CacheService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 缓存监控控制器
 * 
 * @author shamee
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/monitor/cache")
@Anonymous
public class CacheController {

    private final CacheService cacheService;

    /**
     * 获取缓存状态
     */
    @GetMapping("/status")
    public R<CacheStatusResp> getCacheStatus() {
        return R.success(cacheService.getCacheStatus());
    }

    /**
     * 获取缓存列表
     */
    @PostMapping("/list")
    public R<PageResult<CachePageResp>> getCacheList(@RequestBody PageRequest<CacheQueryRequest> request) {
        return R.success(cacheService.getCacheList(request));
    }

    /**
     * 获取缓存详情
     */
    @GetMapping("/detail/{key}")
    public R<Map<String, Object>> getCacheDetail(@PathVariable String key) {
        return R.success(cacheService.getCacheDetail(key));
    }

    /**
     * 删除缓存
     */
    @DeleteMapping("/{key}")
    public R<Void> deleteCache(@PathVariable String key) {
        cacheService.deleteCache(key);
        return R.success();
    }

    /**
     * 批量删除缓存
     */
    @PostMapping("/batch-delete")
    public R<Void> batchDeleteCache(@RequestBody Map<String, Object> params) {
        String[] keys = (String[]) params.get("keys");
        cacheService.batchDeleteCache(keys);
        return R.success();
    }

    /**
     * 清理过期缓存
     */
    @PostMapping("/clear-expired")
    public R<Void> clearExpiredCache() {
        cacheService.clearExpiredCache();
        return R.success();
    }

    /**
     * 清空所有缓存
     */
    @PostMapping("/flush-all")
    public R<Void> flushAllCache() {
        cacheService.flushAllCache();
        return R.success();
    }

    /**
     * 设置缓存TTL
     */
    @PostMapping("/ttl")
    public R<Void> setCacheTTL(@RequestBody Map<String, Object> params) {
        String key = (String) params.get("key");
        Long ttl = Long.valueOf(params.get("ttl").toString());
        cacheService.setCacheTTL(key, ttl);
        return R.success();
    }

    /**
     * 获取缓存内存历史
     */
    @GetMapping("/memory-history")
    public R<List<Map<String, Object>>> getCacheMemoryHistory(
            @RequestParam(value = "hours", defaultValue = "24") Integer hours) {
        return R.success(cacheService.getCacheMemoryHistory(hours));
    }

    /**
     * 获取缓存命中率历史
     */
    @GetMapping("/hit-rate-history")
    public R<List<Map<String, Object>>> getCacheHitRateHistory(
            @RequestParam(value = "hours", defaultValue = "24") Integer hours) {
        return R.success(cacheService.getCacheHitRateHistory(hours));
    }

    /**
     * 获取缓存操作统计
     */
    @GetMapping("/operation-stats")
    public R<Map<String, Object>> getCacheOperationStats(
            @RequestParam(value = "hours", defaultValue = "24") Integer hours) {
        return R.success(cacheService.getCacheOperationStats(hours));
    }

    /**
     * 获取缓存键分析
     */
    @GetMapping("/key-analysis")
    public R<Map<String, Object>> getCacheKeyAnalysis() {
        return R.success(cacheService.getCacheKeyAnalysis());
    }

    /**
     * 获取缓存配置
     */
    @GetMapping("/config")
    public R<Map<String, Object>> getCacheConfig() {
        return R.success(cacheService.getCacheConfig());
    }

    /**
     * 更新缓存配置
     */
    @PutMapping("/config")
    public R<Void> updateCacheConfig(@RequestBody Map<String, Object> config) {
        cacheService.updateCacheConfig(config);
        return R.success();
    }

    /**
     * 获取缓存性能指标
     */
    @GetMapping("/performance-metrics")
    public R<Map<String, Object>> getCachePerformanceMetrics() {
        return R.success(cacheService.getCachePerformanceMetrics());
    }

    /**
     * 获取缓存慢日志
     */
    @GetMapping("/slow-log")
    public R<List<Map<String, Object>>> getCacheSlowLog(
            @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return R.success(cacheService.getCacheSlowLog(pageNo, pageSize));
    }

    /**
     * 获取缓存客户端信息
     */
    @GetMapping("/client-info")
    public R<List<Map<String, Object>>> getCacheClientInfo() {
        return R.success(cacheService.getCacheClientInfo());
    }

    /**
     * 获取缓存备份列表
     */
    @GetMapping("/backup-list")
    public R<List<Map<String, Object>>> getCacheBackupList() {
        return R.success(cacheService.getCacheBackupList());
    }

    /**
     * 创建缓存备份
     */
    @PostMapping("/backup")
    public R<Void> createCacheBackup(@RequestBody Map<String, Object> params) {
        String name = (String) params.get("name");
        String description = (String) params.get("description");
        cacheService.createCacheBackup(name, description);
        return R.success();
    }

    /**
     * 恢复缓存备份
     */
    @PostMapping("/restore")
    public R<Void> restoreCacheBackup(@RequestBody Map<String, Object> params) {
        String backupId = (String) params.get("backupId");
        cacheService.restoreCacheBackup(backupId);
        return R.success();
    }

    /**
     * 导出缓存数据
     */
    @PostMapping("/export")
    public R<String> exportCacheData(@RequestBody Map<String, Object> params) {
        String pattern = (String) params.get("pattern");
        String format = (String) params.get("format");
        return R.success(cacheService.exportCacheData(pattern, format));
    }

    /**
     * 导入缓存数据
     */
    @PostMapping("/import")
    public R<Void> importCacheData(@RequestBody Map<String, Object> params) {
        String data = (String) params.get("data");
        String format = (String) params.get("format");
        Boolean overwrite = (Boolean) params.get("overwrite");
        cacheService.importCacheData(data, format, overwrite);
        return R.success();
    }
}