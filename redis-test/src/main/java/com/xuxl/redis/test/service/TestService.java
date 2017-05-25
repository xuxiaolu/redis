package com.xuxl.redis.test.service;

import com.xuxl.redis.client.CacheKey;
import com.xuxl.redis.client.RedisClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class TestService {

    @Resource
    private RedisClient redisClient;


    public void put(final String key, final String val) {
        CacheKey cacheKey = new CacheKey("test", key);
        redisClient.set(cacheKey, val);
    }

    public String get(final String key) {
        CacheKey cacheKey = new CacheKey("test", key);
        return redisClient.get(cacheKey);
    }


}
