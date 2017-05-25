package com.xuxl.redis.client;

import com.xuxl.redis.client.commands.*;
import com.ymm.redis.client.commands.*;

public interface RedisClient extends RedisExecutor, RedisKeyCommands, RedisStringCommands, RedisHashCommands, RedisListCommands, RedisSetCommands, RedisZSetCommands {

    enum CommandType {

        KEY("Redis.command.key"),
        STRING("Redis.command.string"),
        HASH("Redis.command.hash"),
        SET("Redis.command.set"),
        LIST("Redis.command.list"),
        ZSET("Redis.command.zset");

        private String type;

        CommandType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }
}
