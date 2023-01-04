package com.douyin.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.BooleanUtil;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static com.douyin.util.RedisIdentification.LOCK_CACHE_TTL;

/**
 * @author foanxi
 */
@SuppressWarnings("UnstableApiUsage")
@Component
public class RedisUtil {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * create函数参数解释
     * 第一个参数表示创建一个存储什么类型的布隆过滤器
     * 第二个参数表示布隆过滤器能容纳多大的容器
     * 第三个参数表示容忍误判概率
     */
    private final BloomFilter<String> bloomFilter = BloomFilter.create(Funnels.stringFunnel(Charset.defaultCharset()), 1000, 0.01);

    /**
     * 获取互斥锁
     *
     * @param key 获取互斥锁的业务key
     * @return 返回是否获取成功
     */
    private boolean tryLock(String key) {
        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(key, "1", LOCK_CACHE_TTL, TimeUnit.SECONDS);
        return BooleanUtil.isTrue(flag);
    }

    private long getRandomTime() {
        return (long) (Math.random() * 60 + 1.0);
    }

    /**
     * 将key放入bloom过滤器中
     *
     * @param key 放入布隆过滤器中的key
     */
    public void putInBloom(String key) {
        bloomFilter.put(key);
    }

    /**
     * 释放互斥锁
     *
     * @param key 需要释放的对应的互斥锁
     */
    private void unlock(String key) {
        stringRedisTemplate.delete(key);
    }

    /**
     * 将数据放入redis中
     *
     * @param key      需要放入的key值
     * @param data     对应的数据
     * @param time     过期时间
     * @param timeUnit 时间单位
     */
    public void set(String key, Object data, Long time, TimeUnit timeUnit) {
        Map<String, Object> map;
        if (data == null) {
            map = new HashMap<>();
            map.put("cache", "null");
            stringRedisTemplate.opsForHash().putAll(key, map);
            stringRedisTemplate.expire(key, RedisIdentification.NULL_CACHE_TTL + getRandomTime(), TimeUnit.SECONDS);
        } else {
            map = BeanUtil.beanToMap(data, new HashMap<>(), CopyOptions.create().
                    setIgnoreNullValue(true).
                    setFieldValueEditor((fieldName, fieldValue) -> String.valueOf(fieldValue)));
            bloomFilter.put(key);
            stringRedisTemplate.opsForHash().putAll(key, map);
            stringRedisTemplate.expire(key, time, timeUnit);
        }

    }

    /**
     * 避免缓存穿透的查询方法
     *
     * @param keyPrefix  redis存储前缀
     * @param id         对应实体的id
     * @param dbFallback 回调函数用于查询数据库
     * @param time       过期时间
     * @param timeUnit   时间单位
     * @param <T>        实体类型
     * @return 返回根据id查询的实体类的结果
     */
    public <T> T queryWithoutPassThrough(String keyPrefix, Long id, Class<T> type, Function<Long, T> dbFallback, Long time, TimeUnit timeUnit) {
        String key = keyPrefix + id;
        // 首先检查布隆过滤器内部是否有
        if (!bloomFilter.mightContain(key)) {
            return null;
        }

        // 检查缓存内部是否存在
        Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries(key);
        T t;
        t = BeanUtil.mapToBean(entries, type, true, CopyOptions.create().
                ignoreNullValue().ignoreError().
                setFieldValueEditor((fieldName, fieldValue) -> String.valueOf(fieldValue)));
        if (entries.size() != 0) {
            return t;
        }

        // 缓存中不存在则去数据库查询
        T result = dbFallback.apply(id);

        // 如果数据库中查询存在则缓存重建一个缓存
        this.set(key, result, time, timeUnit);

        return result;
    }

    /**
     * 避免缓存击穿的查询方法
     *
     * @param keyPrefix  redis存储前缀
     * @param id         对应实体的id
     * @param dbFallback 回调函数用于查询数据库
     * @param time       过期时间
     * @param timeUnit   时间单位
     * @return 返回对应id的查询结果
     */
    public <T> T queryWithoutBreakOut(String keyPrefix, Long id, Class<T> type, Function<Long, T> dbFallback, Long time, TimeUnit timeUnit) throws InterruptedException {
        String key = keyPrefix + id;

        while (true) {
            // 第一次或再次查询是否能够命中
            Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries(key);
            T t;
            try {
                t = BeanUtil.fillBeanWithMap(entries, type, true).newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            // 缓存如果命中则直接返回
            if (entries.size() != 0) {
                return t;
            }

            // 未命中尝试获取互斥锁
            String lockKey = RedisIdentification.LOCK_CACHE_KEY + id;
            boolean isLock = tryLock(lockKey);

            // 如果获取成功,则查询数据库重建缓存数据
            if (isLock) {
                try {
                    T result = dbFallback.apply(id);
                    System.out.println(result);
                    this.set(key, result, time, timeUnit);
                } catch (RuntimeException exception) {
                    throw new RuntimeException();
                } finally {
                    // 释放锁
                    unlock(lockKey);
                }
            } else {
                //noinspection BusyWait
                Thread.sleep(1000L);
            }
        }
    }

    /**
     * 删除缓存
     *
     * @param keyPrefix redis存储前缀
     * @param id        对应实体的id
     * @return 返回删除的结果
     */
    public boolean deleteRedisContent(String keyPrefix, String id) {
        String redisId = keyPrefix + id;
        Boolean exists = stringRedisTemplate.hasKey(redisId);
        if (Boolean.TRUE.equals(exists)) {
            stringRedisTemplate.delete(redisId);
            return true;
        }
        return false;
    }
}
