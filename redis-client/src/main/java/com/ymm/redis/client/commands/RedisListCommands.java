package com.ymm.redis.client.commands;


import com.ymm.redis.client.CacheKey;

import java.util.List;
import java.util.concurrent.TimeUnit;

public interface RedisListCommands {

    enum Position {
        BEFORE, AFTER;
    }

    /**
     * Get elements between {@code begin} and {@code end} from list at {@code key}.
     *
     * @param key   must not be {@literal null}.
     * @param start
     * @param end
     * @return
     * @see <a href="http://redis.io/commands/lrange">Redis Documentation: LRANGE</a>
     */
    List<String> lRange(CacheKey key, long start, long end);

    /**
     * Trim list at {@code key} to elements between {@code start} and {@code end}.
     *
     * @param key   must not be {@literal null}.
     * @param start
     * @param end
     * @see <a href="http://redis.io/commands/ltrim">Redis Documentation: LTRIM</a>
     */
    void lTrim(CacheKey key, long start, long end);

    /**
     * Get the size of list stored at {@code key}.
     *
     * @param key must not be {@literal null}.
     * @return
     * @see <a href="http://redis.io/commands/llen">Redis Documentation: LLEN</a>
     */
    Long lLen(CacheKey key);

    /**
     * Prepend {@code value} to {@code key}.
     *
     * @param key    must not be {@literal null}.
     * @param values
     * @return
     * @see <a href="http://redis.io/commands/lpush">Redis Documentation: LPUSH</a>
     */
    Long lPush(CacheKey key, String... values);

    /**
     * Prepend {@code values} to {@code key} only if the list exists.
     *
     * @param key   must not be {@literal null}.
     * @param value
     * @return
     * @see <a href="http://redis.io/commands/lpushx">Redis Documentation: LPUSHX</a>
     */
    Long lPushX(CacheKey key, String value);

    /**
     * Prepend {@code values} to {@code key} before {@code value}.
     *
     * @param key      must not be {@literal null}.
     * @param position
     * @param pivot
     * @param value
     * @return
     * @see <a href="http://redis.io/commands/linsert">Redis Documentation: LINSERT</a>
     */
    Long lInsert(CacheKey key, Position position, String pivot, String value);

    /**
     * Append {@code value} to {@code key}.
     *
     * @param key    must not be {@literal null}.
     * @param values
     * @return
     * @see <a href="http://redis.io/commands/rpush">Redis Documentation: RPUSH</a>
     */
    Long rPush(CacheKey key, String... values);

    /**
     * Append {@code values} to {@code key} only if the list exists.
     *
     * @param key   must not be {@literal null}.
     * @param value
     * @return
     * @see <a href="http://redis.io/commands/rpushx">Redis Documentation: RPUSHX</a>
     */
    Long rPushX(CacheKey key, String value);


    /**
     * Set the {@code value} list element at {@code index}.
     *
     * @param key   must not be {@literal null}.
     * @param index
     * @param value
     * @see <a href="http://redis.io/commands/lset">Redis Documentation: LSET</a>
     */
    void lSet(CacheKey key, long index, String value);

    /**
     * Removes the first {@code count} occurrences of {@code value} from the list stored at {@code key}.
     *
     * @param key   must not be {@literal null}.
     * @param count
     * @param value
     * @return
     * @see <a href="http://redis.io/commands/lrem">Redis Documentation: LREM</a>
     */
    Long lRem(CacheKey key, long count, String value);

    /**
     * Get element at {@code index} form list at {@code key}.
     *
     * @param key   must not be {@literal null}.
     * @param index
     * @return
     * @see <a href="http://redis.io/commands/lindex">Redis Documentation: LINDEX</a>
     */
    String lIndex(CacheKey key, long index);

    /**
     * Removes and returns first element in list stored at {@code key}.
     *
     * @param key must not be {@literal null}.
     * @return
     * @see <a href="http://redis.io/commands/lpop">Redis Documentation: LPOP</a>
     */
    String lPop(CacheKey key);

    /**
     * Removes and returns first element from lists stored at {@code key} . <br>
     * <b>Blocks connection</b> until element available or {@code timeout} reached.
     *
     * @param key     must not be {@literal null}.
     * @param timeout
     * @param unit    must not be {@literal null}.
     * @return
     * @see <a href="http://redis.io/commands/blpop">Redis Documentation: BLPOP</a>
     */
    String bLPop(CacheKey key, long timeout, TimeUnit unit);

    /**
     * Removes and returns last element in list stored at {@code key}.
     *
     * @param key must not be {@literal null}.
     * @return
     * @see <a href="http://redis.io/commands/rpop">Redis Documentation: RPOP</a>
     */
    String rPop(CacheKey key);

    /**
     * Removes and returns last element from lists stored at {@code key}. <br>
     * <b>Blocks connection</b> until element available or {@code timeout} reached.
     *
     * @param key     must not be {@literal null}.
     * @param timeout
     * @param unit    must not be {@literal null}.
     * @return
     * @see <a href="http://redis.io/commands/brpop">Redis Documentation: BRPOP</a>
     */
    String bRPop(CacheKey key, long timeout, TimeUnit unit);

    /**
     * Remove the last element from list at {@code sourceKey}, append it to {@code destinationKey} and return its value.
     *
     * @param sourceKey      must not be {@literal null}.
     * @param destinationKey must not be {@literal null}.
     * @return
     * @see <a href="http://redis.io/commands/rpoplpush">Redis Documentation: RPOPLPUSH</a>
     */
    String rPopLPush(CacheKey sourceKey, CacheKey destinationKey);

    /**
     * Remove the last element from list at {@code srcKey}, append it to {@code dstKey} and return its value.<br>
     * <b>Blocks connection</b> until element available or {@code timeout} reached.
     *
     * @param sourceKey      must not be {@literal null}.
     * @param destinationKey must not be {@literal null}.
     * @param timeout
     * @param unit           must not be {@literal null}.
     * @see <a href="http://redis.io/commands/brpoplpush">Redis Documentation: BRPOPLPUSH</a>
     */
    String bRPopLPush(CacheKey sourceKey, CacheKey destinationKey, long timeout, TimeUnit unit);
}
