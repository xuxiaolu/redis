package com.ymm.redis.client.commands;


import com.ymm.redis.client.CacheKey;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.Collection;
import java.util.Set;

public interface RedisZSetCommands {


    /**
     * Add {@code value} to a sorted set at {@code key}, or update its {@code score} if it already exists.
     *
     * @param key   must not be {@literal null}.
     * @param score the score.
     * @param value the value.
     * @return
     * @see <a href="http://redis.io/commands/zadd">Redis Documentation: ZADD</a>
     */
    Boolean zAdd(CacheKey key, String value, double score);

    /**
     * Add {@code tuples} to a sorted set at {@code key}, or update its {@code score} if it already exists.
     *
     * @param key    must not be {@literal null}.
     * @param tuples must not be {@literal null}.
     * @return
     * @see <a href="http://redis.io/commands/zadd">Redis Documentation: ZADD</a>
     */
    Long zAdd(CacheKey key, Set<ZSetOperations.TypedTuple<String>> tuples);

    /**
     * Remove {@code values} from sorted set. Return number of removed elements.
     *
     * @param key    must not be {@literal null}.
     * @param values must not be {@literal null}.
     * @return
     * @see <a href="http://redis.io/commands/zrem">Redis Documentation: ZREM</a>
     */
    Long zRem(CacheKey key, String... values);

    /**
     * Increment the score of element with {@code value} in sorted set by {@code increment}.
     *
     * @param key   must not be {@literal null}.
     * @param delta
     * @param value the value.
     * @return
     * @see <a href="http://redis.io/commands/zincrby">Redis Documentation: ZINCRBY</a>
     */
    Double zIncrBy(CacheKey key, String value, double delta);

    /**
     * Determine the index of element with {@code value} in a sorted set.
     *
     * @param key must not be {@literal null}.
     * @param o   the value.
     * @return
     * @see <a href="http://redis.io/commands/zrank">Redis Documentation: ZRANK</a>
     */
    Long zRank(CacheKey key, String o);

    /**
     * Determine the index of element with {@code value} in a sorted set when scored high to low.
     *
     * @param key must not be {@literal null}.
     * @param o   the value.
     * @return
     * @see <a href="http://redis.io/commands/zrevrank">Redis Documentation: ZREVRANK</a>
     */
    Long zRevRank(CacheKey key, String o);

    /**
     * Get elements between {@code start} and {@code end} from sorted set.
     *
     * @param key   must not be {@literal null}.
     * @param start
     * @param end
     * @return
     * @see <a href="http://redis.io/commands/zrange">Redis Documentation: ZRANGE</a>
     */
    Set<String> zRange(CacheKey key, long start, long end);

    /**
     *
     * @param key   must not be {@literal null}.
     * @param start
     * @param end
     * @return
     * @see <a href="http://redis.io/commands/zrange">Redis Documentation: ZRANGE</a>
     */
    Set<ZSetOperations.TypedTuple<String>> zRangeWithScores(CacheKey key, long start, long end);

    /**
     * Get elements where score is between {@code min} and {@code max} from sorted set.
     *
     * @param key must not be {@literal null}.
     * @param min
     * @param max
     * @return
     * @see <a href="http://redis.io/commands/zrangebyscore">Redis Documentation: ZRANGEBYSCORE</a>
     */
    Set<String> zRangeByScore(CacheKey key, double min, double max);

    /**
     *
     * @param key must not be {@literal null}.
     * @param min
     * @param max
     * @return
     * @see <a href="http://redis.io/commands/zrangebyscore">Redis Documentation: ZRANGEBYSCORE</a>
     */
    Set<ZSetOperations.TypedTuple<String>> zRangeByScoreWithScores(CacheKey key, double min, double max);

    /**
     * Get elements in range from {@code start} to {@code end} where score is between {@code min} and {@code max} from
     * sorted set.
     *
     * @param key    must not be {@literal null}.
     * @param min
     * @param max
     * @param offset
     * @param count
     * @return
     * @see <a href="http://redis.io/commands/zrangebyscore">Redis Documentation: ZRANGEBYSCORE</a>
     */
    Set<String> zRangeByScore(CacheKey key, double min, double max, long offset, long count);

    /**
     * {@code max} from sorted set.
     *
     * @param key
     * @param min
     * @param max
     * @param offset
     * @param count
     * @return
     * @see <a href="http://redis.io/commands/zrangebyscore">Redis Documentation: ZRANGEBYSCORE</a>
     */
    Set<ZSetOperations.TypedTuple<String>> zRangeByScoreWithScores(CacheKey key, double min, double max, long offset, long count);

    /**
     * Get elements in range from {@code start} to {@code end} from sorted set ordered from high to low.
     *
     * @param key   must not be {@literal null}.
     * @param start
     * @param end
     * @return
     * @see <a href="http://redis.io/commands/zrevrange">Redis Documentation: ZREVRANGE</a>
     */
    Set<String> zRevRange(CacheKey key, long start, long end);

    /**
     *
     * @param key   must not be {@literal null}.
     * @param start
     * @param end
     * @return
     * @see <a href="http://redis.io/commands/zrevrange">Redis Documentation: ZREVRANGE</a>
     */
    Set<ZSetOperations.TypedTuple<String>> zRevRangeWithScores(CacheKey key, long start, long end);

    /**
     * Get elements where score is between {@code min} and {@code max} from sorted set ordered from high to low.
     *
     * @param key must not be {@literal null}.
     * @param min
     * @param max
     * @return
     * @see <a href="http://redis.io/commands/zrevrange">Redis Documentation: ZREVRANGE</a>
     */
    Set<String> zRevRangeByScore(CacheKey key, double min, double max);

    /**
     *
     * @param key must not be {@literal null}.
     * @param min
     * @param max
     * @return
     * @see <a href="http://redis.io/commands/zrevrangebyscore">Redis Documentation: ZREVRANGEBYSCORE</a>
     */
    Set<ZSetOperations.TypedTuple<String>> zRevRangeByScoreWithScores(CacheKey key, double min, double max);

    /**
     * Get elements in range from {@code start} to {@code end} where score is between {@code min} and {@code max} from
     * sorted set ordered high -> low.
     *
     * @param key    must not be {@literal null}.
     * @param min
     * @param max
     * @param offset
     * @param count
     * @return
     * @see <a href="http://redis.io/commands/zrevrangebyscore">Redis Documentation: ZREVRANGEBYSCORE</a>
     */
    Set<String> zRevRangeByScore(CacheKey key, double min, double max, long offset, long count);

    /**
     *
     * @param key    must not be {@literal null}.
     * @param min
     * @param max
     * @param offset
     * @param count
     * @return
     * @see <a href="http://redis.io/commands/zrevrangebyscore">Redis Documentation: ZREVRANGEBYSCORE</a>
     */
    Set<ZSetOperations.TypedTuple<String>> zRevRangeByScoreWithScores(CacheKey key, double min, double max, long offset, long count);

    /**
     * Count number of elements within sorted set with scores between {@code min} and {@code max}.
     *
     * @param key must not be {@literal null}.
     * @param min
     * @param max
     * @return
     * @see <a href="http://redis.io/commands/zcount">Redis Documentation: ZCOUNT</a>
     */
    Long zCount(CacheKey key, double min, double max);

    /**
     * Get the size of sorted set with {@code key}.
     *
     * @param key must not be {@literal null}.
     * @return
     * @see <a href="http://redis.io/commands/zcard">Redis Documentation: ZCARD</a>
     */
    Long zCard(CacheKey key);


    /**
     * Get the score of element with {@code value} from sorted set with key {@code key}.
     *
     * @param key must not be {@literal null}.
     * @param o   the value.
     * @return
     * @see <a href="http://redis.io/commands/zscore">Redis Documentation: ZSCORE</a>
     */
    Double zScore(CacheKey key, String o);

    /**
     * Remove elements in range between {@code start} and {@code end} from sorted set with {@code key}.
     *
     * @param key   must not be {@literal null}.
     * @param start
     * @param end
     * @return
     * @see <a href="http://redis.io/commands/zremrangebyrank">Redis Documentation: ZREMRANGEBYRANK</a>
     */
    Long zRemRangeByRank(CacheKey key, long start, long end);

    /**
     * Remove elements with scores between {@code min} and {@code max} from sorted set with {@code key}.
     *
     * @param key must not be {@literal null}.
     * @param min
     * @param max
     * @return
     * @see <a href="http://redis.io/commands/zremrangebyscore">Redis Documentation: ZREMRANGEBYSCORE</a>
     */
    Long zRemRangeByScore(CacheKey key, double min, double max);

    /**
     * Union sorted sets at {@code key} and {@code otherKeys} and store result in destination {@code destKey}.
     *
     * @param key       must not be {@literal null}.
     * @param otherKeys must not be {@literal null}.
     * @param destKey   must not be {@literal null}.
     * @return
     * @see <a href="http://redis.io/commands/zunionstore">Redis Documentation: ZUNIONSTORE</a>
     */
    Long zUnionStore(CacheKey key, Collection<CacheKey> otherKeys, CacheKey destKey);


    /**
     * Intersect sorted sets at {@code key} and {@code otherKeys} and store result in destination {@code destKey}.
     *
     * @param key       must not be {@literal null}.
     * @param otherKeys must not be {@literal null}.
     * @param destKey   must not be {@literal null}.
     * @return
     * @see <a href="http://redis.io/commands/zinterstore">Redis Documentation: ZINTERSTORE</a>
     */
    Long zInterStore(CacheKey key, Collection<CacheKey> otherKeys, CacheKey destKey);


    /**
     * @param key   must not be {@literal null}.
     * @param range must not be {@literal null}.
     * @see <a href="http://redis.io/commands/zrangebylex">Redis Documentation: ZRANGEBYLEX</a>
     * @since 1.7
     */
    Set<String> zRangeByLex(CacheKey key, org.springframework.data.redis.connection.RedisZSetCommands.Range range);

    /**
     * @param key   must not be {@literal null}
     * @param range must not be {@literal null}.
     * @param limit can be {@literal null}.
     * @return
     * @see <a href="http://redis.io/commands/zrangebylex">Redis Documentation: ZRANGEBYLEX</a>
     */
    Set<String> zRangeByLex(CacheKey key, org.springframework.data.redis.connection.RedisZSetCommands.Range range, org.springframework.data.redis.connection.RedisZSetCommands.Limit limit);
}
