package org.shamee.system.service;

import org.shamee.common.dto.req.PageRequest;
import org.shamee.common.dto.resp.PageResult;
import org.shamee.system.dto.req.monitor.CacheQueryRequest;
import org.shamee.system.dto.resp.monitor.CachePageResp;
import org.shamee.system.dto.resp.monitor.CacheStatusResp;

import java.util.List;
import java.util.Map;

/**
 * 缓存监控服务接口
 * 
 * @author shamee
 */
public interface CacheService {

    /**
     * 获取缓存状态
     */
    CacheStatusResp getCacheStatus();

    /**
     * 获取缓存列表
     */
    PageResult<CachePageResp> getCacheList(PageRequest<CacheQueryRequest> request);

    /**
     * 获取缓存详情
     */
    Map<String, Object> getCacheDetail(String key);

    /**
     * 删除缓存
     */
    void deleteCache(String key);

    /**
     * 批量删除缓存
     */
    void batchDeleteCache(String[] keys);

    /**
     * 清理过期缓存
     */
    void clearExpiredCache();

    /**
     * 清空所有缓存
     */
    void flushAllCache();

    /**
     * 设置缓存TTL
     */
    void setCacheTTL(String key, Long ttl);

    /**
     * 获取缓存内存历史
     */
    List<Map<String, Object>> getCacheMemoryHistory(Integer hours);

    /**
     * 获取缓存命中率历史
     */
    List<Map<String, Object>> getCacheHitRateHistory(Integer hours);

    /**
     * 获取缓存操作统计
     */
    Map<String, Object> getCacheOperationStats(Integer hours);

    /**
     * 获取缓存键分析
     */
    Map<String, Object> getCacheKeyAnalysis();

    /**
     * 获取缓存配置
     */
    Map<String, Object> getCacheConfig();

    /**
     * 更新缓存配置
     */
    void updateCacheConfig(Map<String, Object> config);

    /**
     * 获取缓存性能指标
     */
    Map<String, Object> getCachePerformanceMetrics();

    /**
     * 获取缓存慢日志
     */
    List<Map<String, Object>> getCacheSlowLog(Integer pageNo, Integer pageSize);

    /**
     * 获取缓存客户端信息
     */
    List<Map<String, Object>> getCacheClientInfo();

    /**
     * 获取缓存备份列表
     */
    List<Map<String, Object>> getCacheBackupList();

    /**
     * 创建缓存备份
     */
    void createCacheBackup(String name, String description);

    /**
     * 恢复缓存备份
     */
    void restoreCacheBackup(String backupId);

    /**
     * 导出缓存数据
     */
    String exportCacheData(String pattern, String format);

    /**
     * 导入缓存数据
     */
    void importCacheData(String data, String format, Boolean overwrite);
}