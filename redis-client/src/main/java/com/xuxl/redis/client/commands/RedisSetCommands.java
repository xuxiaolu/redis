package com.xuxl.redis.client.commands;


import com.xuxl.redis.client.CacheKey;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface RedisSetCommands {

    /**
     * Add given {@code values} to set at {@code key}.
     *
     * @param key    must not be {@literal null}.
     * @param values
     * @return
     * @see <a href="http://redis.io/commands/sadd">Redis Documentation: SADD</a>
     */
    Long sAdd(CacheKey key, String... values);

    /**
     * Remove given {@code values} from set at {@code key} and return the number of removed elements.
     *
     * @param key    must not be {@literal null}.
     * @param values
     * @return
     * @see <a href="http://redis.io/commands/srem">Redis Documentation: SREM</a>
     */
    Long sRem(CacheKey key, String... values);

    /**
     * Remove and return a random member from set at {@code key}.
     *
     * @param key must not be {@literal null}.
     * @return
     * @see <a href="http://redis.io/commands/spop">Redis Documentation: SPOP</a>
     */
    String sPop(CacheKey key);

    /**
     * Move {@code value} from {@code key} to {@code destKey}
     *
     * @param key     must not be {@literal null}.
     * @param value
     * @param destKey must not be {@literal null}.
     * @return
     * @see <a href="http://redis.io/commands/smove">Redis Documentation: SMOVE</a>
     */
    Boolean sMove(CacheKey key, String value, CacheKey destKey);

    /**
     * Get size of set at {@code key}.
     *
     * @param key must not be {@literal null}.
     * @return
     * @see <a href="http://redis.io/commands/scard">Redis Documentation: SCARD</a>
     */
    Long sCard(CacheKey key);

    /**
     * Check if set at {@code key} contains {@code value}.
     *
     * @param key must not be {@literal null}.
     * @param o
     * @return
     * @see <a href="http://redis.io/commands/sismember">Redis Documentation: SISMEMBER</a>
     */
    Boolean sIsMember(CacheKey key, String o);


    /**
     * Returns the members intersecting all given sets at {@code key} and {@code otherKeys}.
     *
     * @param key       must not be {@literal null}.
     * @param otherKeys must not be {@literal null}.
     * @return
     * @see <a href="http://redis.io/commands/sinter">Redis Documentation: SINTER</a>
     */
    Set<String> sInter(CacheKey key, Collection<CacheKey> otherKeys);


    /**
     * Intersect all given sets at {@code key} and {@code otherKeys} and store result in {@code destKey}.
     *
     * @param key       must not be {@literal null}.
     * @param otherKeys must not be {@literal null}.
     * @param destKey   must not be {@literal null}.
     * @return
     * @see <a href="http://redis.io/commands/sinterstore">Redis Documentation: SINTERSTORE</a>
     */
    Long sInterStore(CacheKey key, Collection<CacheKey> otherKeys, CacheKey destKey);

    /**
     * Union all sets at given {@code keys} and {@code otherKeys}.
     *
     * @param key       must not be {@literal null}.
     * @param otherKeys must not be {@literal null}.
     * @return
     * @see <a href="http://redis.io/commands/sunion">Redis Documentation: SUNION</a>
     */
    Set<String> sUnion(CacheKey key, Collection<CacheKey> otherKeys);

    /**
     * Union all sets at given {@code key} and {@code otherKeys} and store result in {@code destKey}.
     *
     * @param key       must not be {@literal null}.
     * @param otherKeys must not be {@literal null}.
     * @param destKey   must not be {@literal null}.
     * @return
     * @see <a href="http://redis.io/commands/sunionstore">Redis Documentation: SUNIONSTORE</a>
     */
    Long sUnionStore(CacheKey key, Collection<CacheKey> otherKeys, CacheKey destKey);

    /**
     * Diff all sets for given {@code key} and {@code otherKeys}.
     *
     * @param key       must not be {@literal null}.
     * @param otherKeys must not be {@literal null}.
     * @return
     * @see <a href="http://redis.io/commands/sdiff">Redis Documentation: SDIFF</a>
     */
    Set<String> sDiff(CacheKey key, Collection<CacheKey> otherKeys);

    /**
     * Diff all sets for given {@code key} and {@code otherKeys} and store result in {@code destKey}.
     *
     * @param key       must not be {@literal null}.
     * @param otherKeys must not be {@literal null}.
     * @param destKey   must not be {@literal null}.
     * @return
     * @see <a href="http://redis.io/commands/sdiffstore">Redis Documentation: SDIFFSTORE</a>
     */
    Long sDiffStore(CacheKey key, Collection<CacheKey> otherKeys, CacheKey destKey);

    /**
     * Get all elements of set at {@code key}.
     *
     * @param key must not be {@literal null}.
     * @return
     * @see <a href="http://redis.io/commands/smembers">Redis Documentation: SMEMBERS</a>
     */
    Set<String> sMembers(CacheKey key);

    /**
     * Get random element from set at {@code key}.
     *
     * @param key must not be {@literal null}.
     * @return
     * @see <a href="http://redis.io/commands/srandmember">Redis Documentation: SRANDMEMBER</a>
     */
    String sRandomMember(CacheKey key);


    /**
     * Get {@code count} random elements from set at {@code key}.
     *
     * @param key   must not be {@literal null}.
     * @param count
     * @return
     * @see <a href="http://redis.io/commands/srandmember">Redis Documentation: SRANDMEMBER</a>
     */
    List<String> sRandomMember(CacheKey key, long count);
}
