package com.xuxl.redis.client;

import com.xuxl.redis.client.commands.RedisListCommands;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.*;
import org.springframework.util.Assert;

import java.util.*;
import java.util.concurrent.TimeUnit;


public class SpringRedisClientImpl extends AbstractRedisClientImpl implements InitializingBean {

    private StringRedisTemplate redisTemplate;

    private ValueOperations<String, String> valueOperations;

    private HashOperations<String, String, String> hashOperations;

    private ListOperations<String, String> listOperations;

    private SetOperations<String, String> setOperations;

    private ZSetOperations<String, String> zSetOperations;

    public SpringRedisClientImpl() {
    }

    public SpringRedisClientImpl(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void setRedisTemplate(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.redisTemplate, "redisTemplate is null");
        this.valueOperations = redisTemplate.opsForValue();
        this.hashOperations = redisTemplate.opsForHash();
        this.listOperations = redisTemplate.opsForList();
        this.setOperations = redisTemplate.opsForSet();
        this.zSetOperations = redisTemplate.opsForZSet();
    }

    @Override
    protected void doSet(String finalKey, String value) {
        this.valueOperations.set(finalKey, value);
    }

    @Override
    protected void doSetEX(String finalKey, String value, long timeout, TimeUnit unit) {
        this.valueOperations.set(finalKey, value, timeout, unit);
    }

    @Override
    protected Boolean doSetNX(String finalKey, String value) {
        return this.valueOperations.setIfAbsent(finalKey, value);
    }

    @Override
    protected void domSet(Map<String, String> rawMapKeys) {
        this.valueOperations.multiSet(rawMapKeys);
    }

    @Override
    protected Boolean domSetNX(Map<String, String> rawMapKeys) {
        return this.valueOperations.multiSetIfAbsent(rawMapKeys);
    }

    @Override
    protected String doGet(String finalKey) {
        return this.valueOperations.get(finalKey);
    }

    @Override
    protected String doGetSet(String finalKey, String value) {
        return this.valueOperations.getAndSet(finalKey, value);
    }

    @Override
    protected List<String> domGet(Collection<String> rawKeys) {
        return this.valueOperations.multiGet(rawKeys);
    }

    @Override
    protected Long doIncrBy(String finalKey, long delta) {
        return this.valueOperations.increment(finalKey, delta);
    }

    @Override
    protected Double doIncrByFloat(String finalKey, double delta) {
        return this.valueOperations.increment(finalKey, delta);
    }

    @Override
    protected Integer doAppend(String finalKey, String value) {
        return this.valueOperations.append(finalKey, value);
    }

    @Override
    protected String doGetRange(String finalKey, long start, long end) {
        return this.valueOperations.get(finalKey, start, end);
    }

    @Override
    protected void doSetRange(String finalKey, String value, long offset) {
        this.valueOperations.set(finalKey, value, offset);
    }

    @Override
    protected Long doStrLen(String finalKey) {
        return this.valueOperations.size(finalKey);
    }

    @Override
    protected Boolean doSetBit(String finalKey, long offset, boolean value) {
        return this.valueOperations.setBit(finalKey, offset, value);
    }

    @Override
    protected Boolean doGetBit(String finalKey, long offset) {
        return this.valueOperations.getBit(finalKey, offset);
    }

    @Override
    protected Long dohDel(String finalKey, String[] hashKeys) {
        return this.hashOperations.delete(finalKey, (Object[]) hashKeys);
    }

    @Override
    protected Boolean dohExists(String finalKey, String hashKey) {
        return this.hashOperations.hasKey(finalKey, hashKey);
    }

    @Override
    protected String dohGet(String finalKey, String hashKey) {
        return this.hashOperations.get(finalKey, hashKey);
    }

    @Override
    protected List<String> dohMGet(String finalKey, Collection<String> hashKeys) {
        return this.hashOperations.multiGet(finalKey, hashKeys);
    }

    @Override
    protected Long dohIncrBy(String finalKey, String hashKey, long delta) {
        return this.hashOperations.increment(finalKey, hashKey, delta);
    }

    @Override
    protected Double dohIncrByFloat(String finalKey, String hashKey, double delta) {
        return this.hashOperations.increment(finalKey, hashKey, delta);
    }

    @Override
    protected Set<String> dohKeys(String finalKey) {
        return this.hashOperations.keys(finalKey);
    }

    @Override
    protected Long dohLen(String finalKey) {
        return this.hashOperations.size(finalKey);
    }

    @Override
    protected void dohMSet(String finalKey, Map<String, String> m) {
        this.hashOperations.putAll(finalKey, m);
    }

    @Override
    protected void dohSet(String finalKey, String hashKey, String value) {
        this.hashOperations.put(finalKey, hashKey, value);
    }

    @Override
    protected Boolean dohSetNX(String finalKey, String hashKey, String value) {
        return this.hashOperations.putIfAbsent(finalKey, hashKey, value);
    }

    @Override
    protected List<String> dohVals(String finalKey) {
        return this.hashOperations.values(finalKey);
    }

    @Override
    protected Map<String, String> dohGetAll(String finalKey) {
        return this.hashOperations.entries(finalKey);
    }

    @Override
    protected Boolean doExists(String finalKey) {
        return this.redisTemplate.hasKey(finalKey);
    }

    @Override
    protected void doDel(Collection<String> finalKeys) {
        this.redisTemplate.delete(finalKeys);
    }


    @Override
    protected String doType(String finalKey) {
        return this.redisTemplate.type(finalKey).code();
    }

    @Override
    protected void doReName(String oleKey, String newKey) {
        this.redisTemplate.rename(oleKey, newKey);
    }

    @Override
    protected Boolean doReNameNX(String oleKey, String newKey) {
        return this.redisTemplate.renameIfAbsent(oleKey, newKey);
    }

    @Override
    protected Boolean doExpire(String finalKey, long timeout, TimeUnit unit) {
        return this.redisTemplate.expire(finalKey, timeout, unit);
    }

    @Override
    protected Boolean doExpireAt(String finalKey, Date date) {
        return this.redisTemplate.expireAt(finalKey, date);
    }

    @Override
    protected Boolean doPersist(String finalKey) {
        return this.redisTemplate.persist(finalKey);
    }

    @Override
    protected List<String> dolRang(String finalKey, long start, long end) {
        return this.listOperations.range(finalKey, start, end);
    }

    @Override
    protected void dolTrim(String finalKey, long start, long end) {
        this.listOperations.trim(finalKey, start, end);
    }

    @Override
    protected Long dolLen(String finalKey) {
        return this.listOperations.size(finalKey);
    }

    @Override
    protected Long dolPush(String finalKey, String[] values) {
        return this.listOperations.leftPushAll(finalKey, values);
    }

    @Override
    protected Long dolPushX(String finalKey, String value) {
        return this.listOperations.leftPushIfPresent(finalKey, value);
    }

    @Override
    protected Long dolInsert(String finalKey, RedisListCommands.Position position, String pivot, String value) {
        switch (position) {
            case BEFORE:
                return this.listOperations.leftPush(finalKey, pivot, value);
            case AFTER:
                return this.listOperations.rightPush(finalKey, pivot, value);
            default:
                return null;
        }
    }

    @Override
    protected Long dorPush(String finalKey, String[] values) {
        return this.listOperations.leftPushAll(finalKey, values);
    }

    @Override
    protected Long dorPushX(String finalKey, String value) {
        return this.listOperations.rightPushIfPresent(finalKey, value);
    }

    @Override
    protected void dolSet(String finalKey, long index, String value) {
        this.listOperations.set(finalKey, index, value);
    }

    @Override
    protected Long dolRem(String finalKey, long count, String value) {
        return this.listOperations.remove(finalKey, count, value);
    }

    @Override
    protected String dolIndex(String finalKey, long index) {
        return this.listOperations.index(finalKey, index);
    }

    @Override
    protected String dolPop(String finalKey) {
        return this.listOperations.leftPop(finalKey);
    }

    @Override
    protected String dobLPop(String finalKey, long timeout, TimeUnit unit) {
        return this.listOperations.leftPop(finalKey, timeout, unit);
    }

    @Override
    protected String dorPop(String finalKey) {
        return this.listOperations.rightPop(finalKey);
    }

    @Override
    protected String dobRPop(String finalKey, long timeout, TimeUnit unit) {
        return this.listOperations.rightPop(finalKey, timeout, unit);
    }

    @Override
    protected String dorPopLPush(String sourceRawKey, String destinationRawKey) {
        return this.listOperations.rightPopAndLeftPush(sourceRawKey, destinationRawKey);
    }

    @Override
    protected String dobRPopLPush(String sourceRawKey, String destinationRawKey, long timeout, TimeUnit unit) {
        return this.listOperations.rightPopAndLeftPush(sourceRawKey, destinationRawKey, timeout, unit);
    }

    @Override
    protected Long dosAdd(String finalKey, String[] values) {
        return this.setOperations.add(finalKey, values);
    }

    @Override
    protected Long dosRem(String finalKey, String[] values) {
        return this.setOperations.remove(finalKey, (Object[]) values);
    }

    @Override
    protected String dosPop(String finalKey) {
        return this.setOperations.pop(finalKey);
    }

    @Override
    protected Boolean dosMove(String sourceRawKey, String value, String destRawKey) {
        return this.setOperations.move(sourceRawKey, value, destRawKey);
    }

    @Override
    protected Long dosCard(String finalKey) {
        return this.setOperations.size(finalKey);
    }

    @Override
    protected Boolean dosIsMember(String finalKey, String o) {
        return this.setOperations.isMember(finalKey, o);
    }

    @Override
    protected Set<String> dosInter(String rawKey, Collection<String> otherRawKeys) {
        return this.setOperations.intersect(rawKey, otherRawKeys);
    }

    @Override
    protected Long dosInterStore(String rawKey, Collection<String> otherRawKeys, String destRawKey) {
        return this.setOperations.intersectAndStore(rawKey, otherRawKeys, destRawKey);
    }

    @Override
    protected Set<String> dosUnion(String rawKey, Collection<String> otherRawKeys) {
        return this.setOperations.union(rawKey, otherRawKeys);
    }

    @Override
    protected Long dosUnionStore(String rawKey, Collection<String> otherRawKeys, String destRawKey) {
        return this.setOperations.unionAndStore(rawKey, otherRawKeys, destRawKey);
    }

    @Override
    protected Set<String> dosDiff(String rawKey, Collection<String> otherRawKeys) {
        return this.setOperations.difference(rawKey, otherRawKeys);
    }

    @Override
    protected Long dosDiffStore(String finalKey, Collection<String> otherRawKeys, String destRawKey) {
        return this.setOperations.differenceAndStore(finalKey, otherRawKeys, destRawKey);
    }

    @Override
    protected Set<String> dosMembers(String finalKey) {
        return this.setOperations.members(finalKey);
    }

    @Override
    protected String dosRandomMember(String finalKey) {
        return this.setOperations.randomMember(finalKey);
    }

    @Override
    protected List<String> dosRandomMember(String finalKey, long count) {
        return this.setOperations.randomMembers(finalKey, count);
    }

    @Override
    protected Boolean dozAdd(String finalKey, String value, double score) {
        return this.zSetOperations.add(finalKey, value, score);
    }

    @Override
    protected Long dozAdd(String finalKey, Set<ZSetOperations.TypedTuple<String>> tuples) {
        return this.zSetOperations.add(finalKey, tuples);
    }

    @Override
    protected Long dozRem(String finalKey, String[] values) {
        return this.zSetOperations.remove(finalKey, (Object[]) values);
    }

    @Override
    protected Double dozIncrBy(String finalKey, String value, double delta) {
        return this.zSetOperations.incrementScore(finalKey, value, delta);
    }

    @Override
    protected Long dozRank(String finalKey, String o) {
        return this.zSetOperations.rank(finalKey, o);
    }

    @Override
    protected Long dozRevRank(String finalKey, String o) {
        return this.zSetOperations.reverseRank(finalKey, o);
    }

    @Override
    protected Set<String> dozRange(String finalKey, long start, long end) {
        return this.zSetOperations.range(finalKey, start, end);
    }

    @Override
    protected Set<ZSetOperations.TypedTuple<String>> dozRangeWithScores(String finalKey, long start, long end) {
        return this.zSetOperations.rangeWithScores(finalKey, start, end);

    }

    @Override
    protected Set<String> dozRangeByScore(String finalKey, double min, double max) {
        return this.zSetOperations.rangeByScore(finalKey, min, max);
    }

    @Override
    protected Set<ZSetOperations.TypedTuple<String>> dozRangeByScoreWithScores(String finalKey, double min, double max) {
        return this.zSetOperations.rangeByScoreWithScores(finalKey, min, max);
    }

    @Override
    protected Set<String> dozRangeByScore(String finalKey, double min, double max, long offset, long count) {
        return this.zSetOperations.rangeByScore(finalKey, min, max, offset, count);
    }

    @Override
    protected Set<ZSetOperations.TypedTuple<String>> dozRangeByScoreWithScores(String finalKey, double min, double max, long offset, long count) {
        return this.zSetOperations.rangeByScoreWithScores(finalKey, min, max, offset, count);
    }

    @Override
    protected Set<String> dozRevRange(String finalKey, long start, long end) {
        return this.zSetOperations.reverseRange(finalKey, start, end);
    }

    @Override
    protected Set<ZSetOperations.TypedTuple<String>> dozRevRangeWithScores(String finalKey, long start, long end) {
        return this.zSetOperations.reverseRangeWithScores(finalKey, start, end);
    }

    @Override
    protected Set<String> dozRevRangeByScore(String finalKey, double min, double max) {
        return this.zSetOperations.reverseRangeByScore(finalKey, min, max);
    }

    @Override
    protected Set<ZSetOperations.TypedTuple<String>> dozRevRangeByScoreWithScores(String finalKey, double min, double max) {
        return this.zSetOperations.reverseRangeByScoreWithScores(finalKey, min, max);
    }

    @Override
    protected Set<String> dozRevRangeByScore(String finalKey, double min, double max, long offset, long count) {
        return this.zSetOperations.reverseRangeByScore(finalKey, min, max, offset, count);
    }

    @Override
    protected Set<ZSetOperations.TypedTuple<String>> dozRevRangeByScoreWithScores(String finalKey, double min, double max, long offset, long count) {
        return this.zSetOperations.reverseRangeByScoreWithScores(finalKey, min, max, offset, count);
    }

    @Override
    protected Long dozCount(String finalKey, double min, double max) {
        return this.zSetOperations.count(finalKey, min, max);
    }

    @Override
    protected Long dozCard(String finalKey) {
        return this.zSetOperations.size(finalKey);
    }

    @Override
    protected Double dozScore(String finalKey, String o) {
        return this.zSetOperations.score(finalKey, o);
    }

    @Override
    protected Long dozRemRangeByRank(String finalKey, long start, long end) {
        return this.zSetOperations.removeRange(finalKey, start, end);
    }

    @Override
    protected Long dozRemRangeByScore(String finalKey, double min, double max) {
        return this.zSetOperations.removeRangeByScore(finalKey, min, max);
    }

    @Override
    protected Long dozUnionStore(String finalKey, Collection<String> otherRawKeys, String destRawKey) {
        return this.zSetOperations.unionAndStore(finalKey, otherRawKeys, destRawKey);
    }

    @Override
    protected Long dozInterStore(String finalKey, Collection<String> otherRawKeys, String destRawKey) {
        return this.zSetOperations.intersectAndStore(finalKey, otherRawKeys, destRawKey);
    }

    @Override
    protected Set<String> dozRangeByLex(String finalKey, RedisZSetCommands.Range range) {
        return this.zSetOperations.rangeByLex(finalKey, range);
    }

    @Override
    protected Set<String> dozRangeByLex(String finalKey, RedisZSetCommands.Range range, RedisZSetCommands.Limit limit) {
        return this.zSetOperations.rangeByLex(finalKey, range, limit);
    }

}
