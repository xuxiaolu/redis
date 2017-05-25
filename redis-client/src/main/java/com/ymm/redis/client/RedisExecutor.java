package com.ymm.redis.client;

import redis.clients.jedis.Protocol.Command;

public interface RedisExecutor {


    <T> T execute(CacheKey cacheKey, RedisClient.CommandType commandType, Command command, Executor<T> executor);


    interface Executor<T> {
        T execute(String finalKey);
    }
}
