package com.xuxl.redis.client.commands;


import com.xuxl.redis.client.CacheKey;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RedisHashCommands {

    /**
     * Delete given hash {@code hashKeys}.
     *
     * @param key      must not be {@literal null}.
     * @param hashKeys must not be {@literal null}.
     * @return
     * @see <a href="http://redis.io/commands/hdel">Redis Documentation: HDEL</a>
     */
    Long hDel(CacheKey key, String... hashKeys);

    /**
     * Determine if given hash {@code hashKey} exists.
     *
     * @param key     must not be {@literal null}.
     * @param hashKey must not be {@literal null}.
     * @return
     * @see <a href="http://redis.io/commands/hexits">Redis Documentation: HEXISTS</a>
     */
    Boolean hExists(CacheKey key, String hashKey);

    /**
     * Get value for given {@code hashKey} from hash at {@code key}.
     *
     * @param key     must not be {@literal null}.
     * @param hashKey must not be {@literal null}.
     * @return
     * @see <a href="http://redis.io/commands/hget">Redis Documentation: HGET</a>
     */
    String hGet(CacheKey key, String hashKey);

    /**
     * Get values for given {@code hashKeys} from hash at {@code key}.
     *
     * @param key      must not be {@literal null}.
     * @param hashKeys must not be {@literal null}.
     * @return
     * @see <a href="http://redis.io/commands/hmget">Redis Documentation: HMGET</a>
     */
    List<String> hMGet(CacheKey key, Collection<String> hashKeys);

    /**
     * Increment {@code value} of a hash {@code hashKey} by the given {@code delta}.
     *
     * @param key     must not be {@literal null}.
     * @param hashKey must not be {@literal null}.
     * @param delta
     * @return
     * @see <a href="http://redis.io/commands/hincrby">Redis Documentation: HINCRBY</a>
     */
    Long hIncrBy(CacheKey key, String hashKey, long delta);

    /**
     * Increment {@code value} of a hash {@code hashKey} by the given {@code delta}.
     *
     * @param key     must not be {@literal null}.
     * @param hashKey must not be {@literal null}.
     * @param delta
     * @return
     * @see <a href="http://redis.io/commands/hincrbyfloat">Redis Documentation: HINCRBYFLOAT</a>
     */
    Double hIncrByFloat(CacheKey key, String hashKey, double delta);

    /**
     * Get key set (fields) of hash at {@code key}.
     *
     * @param key must not be {@literal null}.
     * @return
     * @see <a href="http://redis.io/commands/hkeys">Redis Documentation: HKEYS</a>
     */
    Set<String> hKeys(CacheKey key);

    /**
     * Get size of hash at {@code key}.
     *
     * @param key must not be {@literal null}.
     * @return
     * @see <a href="http://redis.io/commands/hlen">Redis Documentation: HLEN</a>
     */
    Long hLen(CacheKey key);

    /**
     * Set multiple hash fields to multiple values using data provided in {@code m}.
     *
     * @param key must not be {@literal null}.
     * @param m   must not be {@literal null}.
     * @see <a href="http://redis.io/commands/hmset">Redis Documentation: HMSET</a>
     */
    void hMSet(CacheKey key, Map<String, String> m);

    /**
     * Set the {@code value} of a hash {@code hashKey}.
     *
     * @param key     must not be {@literal null}.
     * @param hashKey must not be {@literal null}.
     * @param value
     * @see <a href="http://redis.io/commands/hset">Redis Documentation: HSET</a>
     */
    void hSet(CacheKey key, String hashKey, String value);

    /**
     * Set the {@code value} of a hash {@code hashKey} only if {@code hashKey} does not exist.
     *
     * @param key     must not be {@literal null}.
     * @param hashKey must not be {@literal null}.
     * @param value
     * @return
     * @see <a href="http://redis.io/commands/hsetnx">Redis Documentation: HSETNX</a>
     */
    Boolean hSetNX(CacheKey key, String hashKey, String value);

    /**
     * Get entry set (values) of hash at {@code key}.
     *
     * @param key must not be {@literal null}.
     * @return
     * @see <a href="http://redis.io/commands/hvals">Redis Documentation: HVALS</a>
     */
    List<String> hVals(CacheKey key);

    /**
     * Get entire hash stored at {@code key}.
     *
     * @param key must not be {@literal null}.
     * @return
     * @see <a href="http://redis.io/commands/hgetall">Redis Documentation: HGETALL</a>
     */
    Map<String, String> hGetAll(CacheKey key);
}
