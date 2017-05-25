package com.ymm.redis.client.commands;


import com.ymm.redis.client.CacheKey;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public interface RedisStringCommands {

    /**
     * Set {@code value} for {@code key}.
     *
     * @param key   must not be {@literal null}.
     * @param value
     * @see <a href="http://redis.io/commands/set">Redis Documentation: SET</a>
     */
    void set(CacheKey key, String value);

    /**
     * Set the {@code value} and expiration {@code timeout} for {@code key}.
     *
     * @param key     must not be {@literal null}.
     * @param value
     * @param timeout
     * @param unit    must not be {@literal null}.
     * @see <a href="http://redis.io/commands/setex">Redis Documentation: SETEX</a>
     */
    void setEX(CacheKey key, String value, long timeout, TimeUnit unit);

    /**
     * Set {@code key} to hold the string {@code value} if {@code key} is absent.
     *
     * @param key   must not be {@literal null}.
     * @param value
     * @see <a href="http://redis.io/commands/setnx">Redis Documentation: SETNX</a>
     */
    Boolean setNX(CacheKey key, String value);

    /**
     * Set multiple keys to multiple values using key-value pairs provided in {@code tuple}.
     *
     * @param map must not be {@literal null}.
     * @see <a href="http://redis.io/commands/mset">Redis Documentation: MSET</a>
     */
    void mSet(Map<CacheKey, String> map);

    /**
     * Set multiple keys to multiple values using key-value pairs provided in {@code tuple} only if the provided key does
     * not exist.
     *
     * @param map must not be {@literal null}.
     * @see <a href="http://redis.io/commands/msetnx">Redis Documentation: MSETNX</a>
     */
    Boolean mSetNX(Map<CacheKey, String> map);

    /**
     * Get the value of {@code key}.
     *
     * @param key must not be {@literal null}.
     * @see <a href="http://redis.io/commands/get">Redis Documentation: GET</a>
     */
    String get(CacheKey key);

    /**
     * Set {@code value} of {@code key} and return its old value.
     *
     * @param key must not be {@literal null}.
     * @see <a href="http://redis.io/commands/getset">Redis Documentation: GETSET</a>
     */
    String getSet(CacheKey key, String value);

    /**
     * Get multiple {@code keys}. Values are returned in the order of the requested keys.
     *
     * @param keys must not be {@literal null}.
     * @see <a href="http://redis.io/commands/mget">Redis Documentation: MGET</a>
     */
    List<String> mGet(Collection<CacheKey> keys);

    /**
     * Increment an integer value stored as string value under {@code key} by {@code delta}.
     *
     * @param key   must not be {@literal null}.
     * @param delta
     * @see <a href="http://redis.io/commands/incrby">Redis Documentation: INCRBY</a>
     */
    Long incrBy(CacheKey key, long delta);

    /**
     * Increment a floating point number value stored as string value under {@code key} by {@code delta}.
     *
     * @param key   must not be {@literal null}.
     * @param delta
     * @see <a href="http://redis.io/commands/incrbyfloat">Redis Documentation: INCRBYFLOAT</a>
     */
    Double incrByFloat(CacheKey key, double delta);

    /**
     * Append a {@code value} to {@code key}.
     *
     * @param key   must not be {@literal null}.
     * @param value
     * @see <a href="http://redis.io/commands/append">Redis Documentation: APPEND</a>
     */
    Integer append(CacheKey key, String value);

    /**
     * Get a substring of value of {@code key} between {@code begin} and {@code end}.
     *
     * @param key   must not be {@literal null}.
     * @param start
     * @param end
     * @see <a href="http://redis.io/commands/getrange">Redis Documentation: GETRANGE</a>
     */
    String getRange(CacheKey key, long start, long end);

    /**
     * Overwrite parts of {@code key} starting at the specified {@code offset} with given {@code value}.
     *
     * @param key    must not be {@literal null}.
     * @param value
     * @param offset
     * @see <a href="http://redis.io/commands/setrange">Redis Documentation: SETRANGE</a>
     */
    void setRange(CacheKey key, String value, long offset);

    /**
     * Get the length of the value stored at {@code key}.
     *
     * @param key must not be {@literal null}.
     * @see <a href="http://redis.io/commands/strlen">Redis Documentation: STRLEN</a>
     */
    Long strLen(CacheKey key);

    /**
     * Sets the bit at {@code offset} in value stored at {@code key}.
     *
     * @param key    must not be {@literal null}.
     * @param offset
     * @param value
     * @see <a href="http://redis.io/commands/setbit">Redis Documentation: SETBIT</a>
     */
    Boolean setBit(CacheKey key, long offset, boolean value);

    /**
     * Get the bit value at {@code offset} of value at {@code key}.
     *
     * @param key    must not be {@literal null}.
     * @param offset
     * @see <a href="http://redis.io/commands/setbit">Redis Documentation: GETBIT</a>
     */
    Boolean getBit(CacheKey key, long offset);

}
