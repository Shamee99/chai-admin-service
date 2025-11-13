package org.shamee.common.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.core.text.StrUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis工具类
 *
 * @author shamee
 * @since 2024-01-01
 */
@Getter
@Slf4j
@Component
@RequiredArgsConstructor
public class RedisUtil {

    /**
     * -- GETTER --
     *  获取Redis模板
     *
     * @return RedisTemplate
     */
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 设置缓存
     *
     * @param key 键
     * @param value 值
     */
    public void set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
        } catch (Exception e) {
            log.error("Redis设置缓存失败, key: {}, error: {}", key, e.getMessage());
        }
    }

    /**
     * 设置缓存并指定过期时间
     *
     * @param key 键
     * @param value 值
     * @param timeout 过期时间
     * @param unit 时间单位
     */
    public void set(String key, Object value, long timeout, TimeUnit unit) {
        try {
            redisTemplate.opsForValue().set(key, value, timeout, unit);
        } catch (Exception e) {
            log.error("Redis设置缓存失败, key: {}, timeout: {}, error: {}", key, timeout, e.getMessage());
        }
    }

    /**
     * 获取缓存
     *
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        try {
            return StrUtil.isBlank(key) ? null : redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            log.error("Redis获取缓存失败, key: {}, error: {}", key, e.getMessage());
            return null;
        }
    }

    /**
     * 获取缓存（指定类型）
     *
     * @param key 键
     * @param clazz 类型
     * @param <T> 泛型
     * @return 值
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> clazz) {
        try {
            Object value = get(key);
            return clazz.isInstance(value) ? (T) value : null;
        } catch (Exception e) {
            log.error("Redis获取缓存失败, key: {}, class: {}, error: {}", key, clazz.getSimpleName(), e.getMessage());
            return null;
        }
    }

    /**
     * 删除缓存
     *
     * @param key 键
     * @return 是否删除成功
     */
    public boolean delete(String key) {
        try {
            return StrUtil.isNotBlank(key) && Boolean.TRUE.equals(redisTemplate.delete(key));
        } catch (Exception e) {
            log.error("Redis删除缓存失败, key: {}, error: {}", key, e.getMessage());
            return false;
        }
    }

    /**
     * 批量删除缓存
     *
     * @param keys 键集合
     * @return 删除的数量
     */
    public long delete(String... keys) {
        try {
            if (keys == null || keys.length == 0) {
                return 0;
            }
            return redisTemplate.delete(java.util.Arrays.asList(keys));
        } catch (Exception e) {
            log.error("Redis批量删除缓存失败, error: {}", e.getMessage());
            return 0;
        }
    }

    /**
     * 判断键是否存在
     *
     * @param key 键
     * @return 是否存在
     */
    public boolean hasKey(String key) {
        try {
            return StrUtil.isNotBlank(key) && Boolean.TRUE.equals(redisTemplate.hasKey(key));
        } catch (Exception e) {
            log.error("Redis判断键是否存在失败, key: {}, error: {}", key, e.getMessage());
            return false;
        }
    }

    /**
     * 设置过期时间
     *
     * @param key 键
     * @param timeout 过期时间
     * @param unit 时间单位
     * @return 是否设置成功
     */
    public boolean expire(String key, long timeout, TimeUnit unit) {
        try {
            return StrUtil.isNotBlank(key) && Boolean.TRUE.equals(redisTemplate.expire(key, timeout, unit));
        } catch (Exception e) {
            log.error("Redis设置过期时间失败, key: {}, timeout: {}, error: {}", key, timeout, e.getMessage());
            return false;
        }
    }

    /**
     * 获取过期时间
     *
     * @param key 键
     * @return 过期时间（秒），-1表示永不过期，-2表示键不存在
     */
    public long getExpire(String key) {
        try {
            Long expire = redisTemplate.getExpire(key, TimeUnit.SECONDS);
            return expire != null ? expire : -2;
        } catch (Exception e) {
            log.error("Redis获取过期时间失败, key: {}, error: {}", key, e.getMessage());
            return -2;
        }
    }

    /**
     * 递增
     *
     * @param key 键
     * @param delta 递增值
     * @return 递增后的值
     */
    public long increment(String key, long delta) {
        try {
            Long result = redisTemplate.opsForValue().increment(key, delta);
            return result != null ? result : 0;
        } catch (Exception e) {
            log.error("Redis递增失败, key: {}, delta: {}, error: {}", key, delta, e.getMessage());
            return 0;
        }
    }

    /**
     * 递减
     *
     * @param key 键
     * @param delta 递减值
     * @return 递减后的值
     */
    public long decrement(String key, long delta) {
        try {
            Long result = redisTemplate.opsForValue().decrement(key, delta);
            return result != null ? result : 0;
        } catch (Exception e) {
            log.error("Redis递减失败, key: {}, delta: {}, error: {}", key, delta, e.getMessage());
            return 0;
        }
    }


    public Set<String> keys(final String pattern) {
        return redisTemplate.keys(pattern);
    }

}