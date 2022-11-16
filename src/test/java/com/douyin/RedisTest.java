package com.douyin;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.douyin.mapper.UserMapper;
import com.douyin.pojo.User;
import com.douyin.util.RedisIdentification;
import com.douyin.util.RedisUtil;
import com.douyin.util.SnowFlake;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("UnstableApiUsage")
@SpringBootTest
public class RedisTest {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private UserMapper userMapper;

    private final BloomFilter<String> bloomFilter = BloomFilter.create(Funnels.stringFunnel(Charset.defaultCharset()), 1000, 0.01);

    @Test
    void testSave() {
        User user = new User();
        user.setUserId(SnowFlake.nextId());
        user.setName("foanxi");
        user.setPassword("123456");
        String userInsertKey = RedisIdentification.USER_QUERY_KEY + user.getUserId();
        redisUtil.set(userInsertKey, user, RedisIdentification.USER_QUERY_TTL, TimeUnit.MINUTES);
    }

    @Test
    void testSaveUser() {
        User user = new User();
        user.setUserId(SnowFlake.nextId());
        user.setName("foanxi");
        user.setPassword("123456");
        Map<String, Object> map = BeanUtil.beanToMap(user, new HashMap<>(), CopyOptions.create().
                setIgnoreNullValue(true).
                setFieldValueEditor((fieldName, fieldValue) -> String.valueOf(fieldValue)));
        String userInsertKey = RedisIdentification.USER_QUERY_KEY + user.getUserId();
        bloomFilter.put(userInsertKey);
        stringRedisTemplate.opsForHash().putAll(userInsertKey, map);
        stringRedisTemplate.expire(userInsertKey, RedisIdentification.USER_QUERY_TTL, TimeUnit.MINUTES);
    }

    @Test
    void testQueryUserByWithoutBreakout() throws InterruptedException {
        Long id = 790193544949137408L;
        User user = redisUtil.queryWithoutBreakOut(RedisIdentification.USER_QUERY_KEY, id, User.class, userMapper::selectById, RedisIdentification.USER_QUERY_TTL, TimeUnit.MINUTES);
        System.out.println(user);
    }
}
