package com.ymm.redis.client.commands;


import com.ymm.redis.client.CacheKey;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public interface RedisKeyCommands {


    /**
     * Determine if given {@code key} exists.
     *
     * @param key must not be {@literal null}.
     * @return
     * @see <a href="http://redis.io/commands/exists">Redis Documentation: EXISTS</a>
     */
    Boolean exists(CacheKey key);

    /**
     * Delete given {@code keys}.
     *
     * @param keys must not be {@literal null}.
     * @return The number of keys that were removed.
     * @see <a href="http://redis.io/commands/del">Redis Documentation: DEL</a>
     */
    void del(CacheKey... keys);

    /**
     * Determine the type stored at {@code key}.
     *
     * @param key must not be {@literal null}.
     * @return
     * @see <a href="http://redis.io/commands/type">Redis Documentation: TYPE</a>
     */
    String type(CacheKey key);

    /**
     * Rename key {@code oldName} to {@code newName}.
     *
     * @param oldName must not be {@literal null}.
     * @param newName must not be {@literal null}.
     * @see <a href="http://redis.io/commands/rename">Redis Documentation: RENAME</a>
     */
    void rename(CacheKey oldName, CacheKey newName);

    /**
     * Rename key {@code oldName} to {@code newName} only if {@code newName} does not exist.
     *
     * @param oldName must not be {@literal null}.
     * @param newName must not be {@literal null}.
     * @return
     * @see <a href="http://redis.io/commands/renamenx">Redis Documentation: RENAMENX</a>
     */
    Boolean renameNX(CacheKey oldName, CacheKey newName);

    /**
     * Set time to live for given {@code key} in seconds.
     *
     * @param key     must not be {@literal null}.
     * @param timeout
     * @param unit
     * @return
     * @see <a href="http://redis.io/commands/expire">Redis Documentation: EXPIRE</a>
     */
    Boolean expire(CacheKey key, long timeout, TimeUnit unit);


    /**
     * Set the expiration for given {@code key} as a {@literal UNIX} timestamp.
     *
     * @param key  must not be {@literal null}.
     * @param date
     * @return
     * @see <a href="http://redis.io/commands/expireat">Redis Documentation: EXPIREAT</a>
     */
    Boolean expireAt(CacheKey key, Date date);

    /**
     * Remove the expiration from given {@code key}.
     *
     * @param key must not be {@literal null}.
     * @return
     * @see <a href="http://redis.io/commands/persist">Redis Documentation: PERSIST</a>
     */
    Boolean persist(CacheKey key);

}
