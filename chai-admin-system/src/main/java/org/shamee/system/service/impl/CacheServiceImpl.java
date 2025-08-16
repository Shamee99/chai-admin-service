package org.shamee.system.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.shamee.common.dto.req.PageRequest;
import org.shamee.common.dto.resp.PageResult;
import org.shamee.system.dto.req.monitor.CacheQueryRequest;
import org.shamee.system.dto.resp.monitor.CachePageResp;
import org.shamee.system.dto.resp.monitor.CacheStatusResp;
import org.shamee.system.service.CacheService;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 缓存监控服务实现类
 * 
 * @author shamee
 */
@Service
@RequiredArgsConstructor
public class CacheServiceImpl implements CacheService {

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public CacheStatusResp getCacheStatus() {
        CacheStatusResp status = new CacheStatusResp();
        try {
            Properties info = Objects.requireNonNull(redisTemplate.getConnectionFactory())
                    .getConnection()
                    .info();

            // 设置连接成功
            status.setConnected(true);
            status.setVersion(info.getProperty("redis_version"));

            // 内存信息（字节 → MB）
            long usedMemoryBytes = Long.parseLong(info.getProperty("used_memory", "0"));
            long maxMemoryBytes = Long.parseLong(info.getProperty("maxmemory", "-1"));

            status.setUsedMemoryMB(roundTo2Decimal(usedMemoryBytes / 1024.0 / 1024.0));
            status.setMaxMemoryMB(maxMemoryBytes <= 0 ? null : roundTo2Decimal(maxMemoryBytes / 1024.0 / 1024.0));

            // 解析 keyspace（如 db0: keys=100,expires=10,...）
            String db0Info = info.getProperty("db0");
            if (db0Info != null && !db0Info.isEmpty()) {
                Map<String, String> kv = Arrays.stream(db0Info.split(","))
                        .map(s -> s.split("=", 2))
                        .collect(Collectors.toMap(
                                arr -> arr[0].trim(),
                                arr -> arr.length > 1 ? arr[1].trim() : "0"
                        ));
                status.setKeyCount(Long.valueOf(kv.getOrDefault("keys", "0")));
                status.setExpiredKeys(Long.valueOf(kv.getOrDefault("expires", "0")));
            } else {
                status.setKeyCount(0L);
                status.setExpiredKeys(0L);
            }

            // 命中统计
            long hits = Long.parseLong(info.getProperty("keyspace_hits", "0"));
            long misses = Long.parseLong(info.getProperty("keyspace_misses", "0"));
            long total = hits + misses;

            status.setHits(hits);
            status.setTotalCommands(total);
            status.setHitRate(String.format("%.2f%%", total > 0 ? (double) hits / total * 100 : 0));

            return status;
        }catch (Exception e) {
            status.setConnected(false);
            return status;
        }
    }

    private Double roundTo2Decimal(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    @Override
    public PageResult<CachePageResp> getCacheList(PageRequest<CacheQueryRequest> request) {
        List<CachePageResp> cacheList = new ArrayList<>();
        int pageNo = request.getPageNo();
        int pageSize = request.getPageSize();

        // 获取匹配的 key（注意：生产环境慎用 keys *）
        String pattern = request.getParam().getPattern();
        if (pattern == null || pattern.isBlank()) {
            pattern = "*";
        }

        Set<String> keys = redisTemplate.keys(pattern);
        if (keys.isEmpty()) {
            Page<CachePageResp> page = new Page<>(pageNo, pageSize);
            page.setRecords(new ArrayList<>());
            page.setTotal(0);
            return PageResult.empty(CachePageResp.class);
        }

        // 转为列表并分页
        List<String> keyList = new ArrayList<>(keys);
        int total = keyList.size();
        int start = (pageNo - 1) * pageSize;
        int end = Math.min(start + pageSize, total);

        for (int i = start; i < end; i++) {
            String key = keyList.get(i);
            DataType type = redisTemplate.type(key);
            Long ttl = redisTemplate.getExpire(key, TimeUnit.SECONDS);
            Long size = getKeySize(key); // 你已实现的方法

            CachePageResp item = new CachePageResp();
            item.setKey(key);
            item.setType(type.code());
            item.setTtl(ttl); // null 视为不存在
            item.setSize(size);

            cacheList.add(item);
        }

        // 构造分页对象
        Page<CachePageResp> page = new Page<>(pageNo, pageSize);
        page.setRecords(cacheList);
        page.setTotal(total);

        return PageResult.of(page, CachePageResp::new);
    }

    @Override
    public Map<String, Object> getCacheDetail(String key) {
        Map<String, Object> detail = new HashMap<>();
        try {
            detail.put("key", key);
            detail.put("type", redisTemplate.type(key).code());
            detail.put("ttl", redisTemplate.getExpire(key, TimeUnit.SECONDS));
            detail.put("value", redisTemplate.opsForValue().get(key));
            detail.put("size", getKeySize(key));
        } catch (Exception e) {
            detail.put("error", e.getMessage());
        }
        return detail;
    }

    @Override
    public void deleteCache(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public void batchDeleteCache(String[] keys) {
        redisTemplate.delete(Arrays.asList(keys));
    }

    @Override
    public void clearExpiredCache() {
        // Redis会自动清理过期键，这里可以实现自定义逻辑
    }

    @Override
    public void flushAllCache() {
        Objects.requireNonNull(redisTemplate.getConnectionFactory()).getConnection().flushAll();
    }

    @Override
    public void setCacheTTL(String key, Long ttl) {
        redisTemplate.expire(key, ttl, TimeUnit.SECONDS);
    }

    @Override
    public List<Map<String, Object>> getCacheMemoryHistory(Integer hours) {
        List<Map<String, Object>> history = new ArrayList<>();
        // 模拟数据，实际应该从监控数据库获取
        LocalDateTime now = LocalDateTime.now();
        for (int i = hours - 1; i >= 0; i--) {
            Map<String, Object> data = new HashMap<>();
            data.put("time", now.minusHours(i).format(DateTimeFormatter.ofPattern("HH:mm")));
            data.put("used", 50 + new Random().nextInt(30));
            data.put("free", 100 - (Integer) data.get("used"));
            history.add(data);
        }
        return history;
    }

    @Override
    public List<Map<String, Object>> getCacheHitRateHistory(Integer hours) {
        List<Map<String, Object>> history = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        for (int i = hours - 1; i >= 0; i--) {
            Map<String, Object> data = new HashMap<>();
            data.put("time", now.minusHours(i).format(DateTimeFormatter.ofPattern("HH:mm")));
            data.put("hitRate", 85 + new Random().nextInt(10));
            history.add(data);
        }
        return history;
    }

    @Override
    public Map<String, Object> getCacheOperationStats(Integer hours) {
        Map<String, Object> stats = new HashMap<>();
        stats.put("gets", 10000 + new Random().nextInt(5000));
        stats.put("sets", 2000 + new Random().nextInt(1000));
        stats.put("dels", 500 + new Random().nextInt(200));
        stats.put("expires", 300 + new Random().nextInt(100));
        return stats;
    }

    @Override
    public Map<String, Object> getCacheKeyAnalysis() {
        Map<String, Object> analysis = new HashMap<>();
        analysis.put("totalKeys", 1500);
        analysis.put("expiredKeys", 200);
        analysis.put("keysByType", Map.of(
            "string", 800,
            "hash", 400,
            "list", 200,
            "set", 100
        ));
        return analysis;
    }

    @Override
    public Map<String, Object> getCacheConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put("maxMemory", "2gb");
        config.put("maxMemoryPolicy", "allkeys-lru");
        config.put("timeout", 0);
        config.put("databases", 16);
        return config;
    }

    @Override
    public void updateCacheConfig(Map<String, Object> config) {
        // 实现配置更新逻辑
    }

    @Override
    public Map<String, Object> getCachePerformanceMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("opsPerSecond", 1000 + new Random().nextInt(500));
        metrics.put("avgResponseTime", 0.5 + new Random().nextDouble());
        metrics.put("memoryFragmentation", 1.2 + new Random().nextDouble() * 0.3);
        return metrics;
    }

    @Override
    public List<Map<String, Object>> getCacheSlowLog(Integer pageNo, Integer pageSize) {
        List<Map<String, Object>> slowLog = new ArrayList<>();
        // 模拟慢日志数据
        for (int i = 0; i < pageSize; i++) {
            Map<String, Object> log = new HashMap<>();
            log.put("id", i + 1);
            log.put("timestamp", LocalDateTime.now().minusMinutes(i * 5L));
            log.put("duration", 100 + new Random().nextInt(900));
            log.put("command", "GET key" + i);
            slowLog.add(log);
        }
        return slowLog;
    }

    @Override
    public List<Map<String, Object>> getCacheClientInfo() {
        List<Map<String, Object>> clients = new ArrayList<>();
        // 模拟客户端信息
        for (int i = 0; i < 5; i++) {
            Map<String, Object> client = new HashMap<>();
            client.put("id", i + 1);
            client.put("addr", "192.168.1." + (100 + i) + ":" + (5000 + i));
            client.put("name", "client-" + i);
            client.put("age", 300 + new Random().nextInt(1000));
            clients.add(client);
        }
        return clients;
    }

    @Override
    public List<Map<String, Object>> getCacheBackupList() {
        List<Map<String, Object>> backups = new ArrayList<>();
        // 模拟备份列表
        for (int i = 0; i < 3; i++) {
            Map<String, Object> backup = new HashMap<>();
            backup.put("id", "backup-" + i);
            backup.put("name", "备份-" + i);
            backup.put("createTime", LocalDateTime.now().minusDays(i));
            backup.put("size", "10MB");
            backups.add(backup);
        }
        return backups;
    }

    @Override
    public void createCacheBackup(String name, String description) {
        // 实现备份创建逻辑
    }

    @Override
    public void restoreCacheBackup(String backupId) {
        // 实现备份恢复逻辑
    }

    @Override
    public String exportCacheData(String pattern, String format) {
        // 实现数据导出逻辑
        return "exported data";
    }

    @Override
    public void importCacheData(String data, String format, Boolean overwrite) {
        // 实现数据导入逻辑
    }

    private long getKeySize(String key) {
        try {
            Long result = redisTemplate.execute(
                    (RedisCallback<Long>) connection -> {
                        Object cmdResult = connection.execute("MEMORY USAGE", key.getBytes());
                        if (cmdResult instanceof byte[]) {
                            return Long.parseLong(new String((byte[]) cmdResult));
                        } else if (cmdResult instanceof Long) {
                            return (Long) cmdResult;
                        } else if (cmdResult instanceof String) {
                            return Long.parseLong((String) cmdResult);
                        } else {
                            return 0L;
                        }
                    }
            );
            return result != null ? result : 0L;
        } catch (Exception e) {
            return 0L;
        }
    }
}