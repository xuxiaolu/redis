package com.xuxl.redis.client;

import com.xuxl.redis.client.commands.RedisListCommands;
import com.xuxl.redis.client.data.ConfigManager;
import com.xuxl.redis.client.data.ClusterEnvironment;
import com.xuxl.redis.client.exception.ClientException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.util.Assert;
import redis.clients.jedis.Protocol;

import java.util.*;
import java.util.concurrent.TimeUnit;

public abstract class AbstractRedisClientImpl implements RedisClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractRedisClientImpl.class);

    private static final String SEPARATOR = "_";

    private String rawKey(CacheKey cacheKey) {
        Assert.notNull(cacheKey, "cacheKey is null");
        String itemKey = cacheKey.getItem();
        Assert.hasText(itemKey, "item is null");
        String item = ConfigManager.getInstance().getItem(itemKey);
        if (StringUtils.isBlank(item)) {
            throw new ClientException(String.format("%s is not exists", itemKey));
        }
        String key = cacheKey.getKey();
        Assert.hasText(key, "key is null");
        return ClusterEnvironment.getBusinessLineName() + SEPARATOR + item + SEPARATOR + key;
    }

    private Collection<String> rawKeys(CacheKey[] keys) {
        Collection<String> targets = new LinkedHashSet<>();
        for (CacheKey key : keys) {
            targets.add(rawKey(key));
        }
        return targets;
    }

    private Collection<String> rawKeys(Collection<CacheKey> keys) {
        Collection<String> targets = new LinkedHashSet<>();
        for (CacheKey key : keys) {
            targets.add(rawKey(key));
        }
        return targets;
    }

    private Map<String, String> rawMapKeys(Map<CacheKey, String> map) {
        Map<String, String> targets = new LinkedHashMap<>();
        for (Map.Entry<CacheKey, String> entry : map.entrySet()) {
            targets.put(rawKey(entry.getKey()), entry.getValue());
        }
        return targets;
    }

    private <T> T execute(CacheKey cacheKey, boolean isUse, RedisClient.CommandType commandType, Protocol.Command command, Executor<T> executor) {
        try {
            String finalKey = "";
            if (isUse) {
                finalKey = rawKey(cacheKey);
            }
            return executor.execute(finalKey);
        } catch (Throwable e) {
            LOGGER.error("{}.{} invoke error", commandType.getType(), command.name());
            throw e;
        }
    }

    @Override
    public <T> T execute(CacheKey cacheKey, RedisClient.CommandType commandType, Protocol.Command command, Executor<T> executor) {
        return execute(cacheKey, true, commandType, command, executor);
    }

    @Override
    public void set(CacheKey key, final String value) {
        execute(key, CommandType.STRING, Protocol.Command.SET, new Executor<Object>() {
            public Object execute(String finalKey) {
                doSet(finalKey, value);
                return null;
            }
        });
    }

    protected abstract void doSet(String finalKey, String value);

    @Override
    public void setEX(CacheKey key, final String value, final long timeout, final TimeUnit unit) {
        execute(key, CommandType.STRING, Protocol.Command.SETEX, new Executor<Object>() {
            public Object execute(String finalKey) {
                doSetEX(finalKey, value, timeout, unit);
                return null;
            }
        });
    }

    protected abstract void doSetEX(String finalKey, String value, long timeout, TimeUnit unit);

    @Override
    public Boolean setNX(CacheKey key, final String value) {
        return execute(key, CommandType.STRING, Protocol.Command.SETNX, new Executor<Boolean>() {
            public Boolean execute(String finalKey) {
                return doSetNX(finalKey, value);
            }
        });
    }

    protected abstract Boolean doSetNX(String finalKey, String value);

    @Override
    public void mSet(final Map<CacheKey, String> map) {
        execute(null, false, CommandType.STRING, Protocol.Command.MSET, new Executor<Boolean>() {
            public Boolean execute(String finalKey) {
                Map<String, String> rawMapKeys = rawMapKeys(map);
                domSet(rawMapKeys);
                return null;
            }
        });
    }

    protected abstract void domSet(Map<String, String> rawMapKeys);

    @Override
    public Boolean mSetNX(final Map<CacheKey, String> map) {
        return execute(null, false, CommandType.STRING, Protocol.Command.MSETNX, new Executor<Boolean>() {
            public Boolean execute(String finalKey) {
                Map<String, String> rawMapKeys = rawMapKeys(map);
                return domSetNX(rawMapKeys);
            }
        });
    }

    protected abstract Boolean domSetNX(Map<String, String> rawMapKeys);

    @Override
    public String get(CacheKey key) {
        return execute(key, CommandType.STRING, Protocol.Command.GET, new Executor<String>() {
            public String execute(String finalKey) {
                return doGet(finalKey);
            }
        });
    }

    protected abstract String doGet(String finalKey);

    @Override
    public String getSet(CacheKey key, final String value) {
        return execute(key, CommandType.STRING, Protocol.Command.GETSET, new Executor<String>() {
            public String execute(String finalKey) {
                return doGetSet(finalKey, value);
            }
        });
    }

    protected abstract String doGetSet(String finalKey, String value);

    @Override
    public List<String> mGet(final Collection<CacheKey> keys) {
        return execute(null, false, CommandType.STRING, Protocol.Command.MGET, new Executor<List<String>>() {
            public List<String> execute(String finalKey) {
                Collection<String> rawKeys = rawKeys(keys);
                return domGet(rawKeys);
            }
        });
    }

    protected abstract List<String> domGet(Collection<String> rawKeys);

    @Override
    public Long incrBy(CacheKey key, final long delta) {
        return execute(key, CommandType.STRING, Protocol.Command.INCRBY, new Executor<Long>() {
            public Long execute(String finalKey) {
                return doIncrBy(finalKey, delta);
            }
        });
    }

    protected abstract Long doIncrBy(String finalKey, long delta);

    @Override
    public Double incrByFloat(CacheKey key, final double delta) {
        return execute(key, CommandType.STRING, Protocol.Command.INCRBYFLOAT, new Executor<Double>() {
            public Double execute(String finalKey) {
                return doIncrByFloat(finalKey, delta);
            }
        });
    }

    protected abstract Double doIncrByFloat(String finalKey, double delta);

    @Override
    public Integer append(CacheKey key, final String value) {
        return execute(key, CommandType.STRING, Protocol.Command.APPEND, new Executor<Integer>() {
            public Integer execute(String finalKey) {
                return doAppend(finalKey, value);
            }
        });
    }

    protected abstract Integer doAppend(String finalKey, String value);

    @Override
    public String getRange(CacheKey key, final long start, final long end) {
        return execute(key, CommandType.STRING, Protocol.Command.GETRANGE, new Executor<String>() {
            public String execute(String finalKey) {
                return doGetRange(finalKey, start, end);
            }
        });
    }

    protected abstract String doGetRange(String finalKey, long start, long end);

    @Override
    public void setRange(CacheKey key, final String value, final long offset) {
        execute(key, CommandType.STRING, Protocol.Command.SETRANGE, new Executor<Object>() {
            public Object execute(String finalKey) {
                doSetRange(finalKey, value, offset);
                return null;
            }
        });
    }

    protected abstract void doSetRange(String finalKey, String value, long offset);

    @Override
    public Long strLen(CacheKey key) {
        return execute(key, CommandType.STRING, Protocol.Command.STRLEN, new Executor<Long>() {
            public Long execute(String finalKey) {
                return doStrLen(finalKey);
            }
        });
    }

    protected abstract Long doStrLen(String finalKey);

    @Override
    public Boolean setBit(CacheKey key, final long offset, final boolean value) {
        return execute(key, CommandType.STRING, Protocol.Command.SETBIT, new Executor<Boolean>() {
            public Boolean execute(String finalKey) {
                return doSetBit(finalKey, offset, value);
            }
        });
    }

    protected abstract Boolean doSetBit(String finalKey, long offset, boolean value);

    @Override
    public Boolean getBit(CacheKey key, final long offset) {
        return execute(key, CommandType.STRING, Protocol.Command.GETBIT, new Executor<Boolean>() {
            public Boolean execute(String finalKey) {
                return doGetBit(finalKey, offset);
            }
        });
    }

    protected abstract Boolean doGetBit(String finalKey, long offset);

    @Override
    public Long hDel(CacheKey key, final String... hashKeys) {
        return execute(key, CommandType.HASH, Protocol.Command.HDEL, new Executor<Long>() {
            public Long execute(String finalKey) {
                return dohDel(finalKey, hashKeys);
            }
        });
    }

    protected abstract Long dohDel(String finalKey, String[] hashKeys);

    @Override
    public Boolean hExists(CacheKey key, final String hashKey) {
        return execute(key, CommandType.HASH, Protocol.Command.HEXISTS, new Executor<Boolean>() {
            public Boolean execute(String finalKey) {
                return dohExists(finalKey, hashKey);
            }
        });
    }

    protected abstract Boolean dohExists(String finalKey, String hashKey);

    @Override
    public String hGet(CacheKey key, final String hashKey) {
        return execute(key, CommandType.HASH, Protocol.Command.HGET, new Executor<String>() {
            public String execute(String finalKey) {
                return dohGet(finalKey, hashKey);
            }
        });
    }

    protected abstract String dohGet(String finalKey, String hashKey);

    @Override
    public List<String> hMGet(CacheKey key, final Collection<String> hashKeys) {
        return execute(key, CommandType.HASH, Protocol.Command.HMGET, new Executor<List<String>>() {
            public List<String> execute(String finalKey) {
                return dohMGet(finalKey, hashKeys);
            }
        });
    }

    protected abstract List<String> dohMGet(String finalKey, Collection<String> hashKeys);

    @Override
    public Long hIncrBy(CacheKey key, final String hashKey, final long delta) {
        return execute(key, CommandType.HASH, Protocol.Command.HINCRBY, new Executor<Long>() {
            public Long execute(String finalKey) {
                return dohIncrBy(finalKey, hashKey, delta);
            }
        });
    }

    protected abstract Long dohIncrBy(String finalKey, String hashKey, long delta);

    @Override
    public Double hIncrByFloat(CacheKey key, final String hashKey, final double delta) {
        return execute(key, CommandType.HASH, Protocol.Command.HINCRBYFLOAT, new Executor<Double>() {
            public Double execute(String finalKey) {
                return dohIncrByFloat(finalKey, hashKey, delta);
            }
        });
    }

    protected abstract Double dohIncrByFloat(String finalKey, String hashKey, double delta);

    @Override
    public Set<String> hKeys(CacheKey key) {
        return execute(key, CommandType.HASH, Protocol.Command.HKEYS, new Executor<Set<String>>() {
            public Set<String> execute(String finalKey) {
                return dohKeys(finalKey);
            }
        });
    }

    protected abstract Set<String> dohKeys(String finalKey);

    @Override
    public Long hLen(CacheKey key) {
        return execute(key, CommandType.HASH, Protocol.Command.HLEN, new Executor<Long>() {
            public Long execute(String finalKey) {
                return dohLen(finalKey);
            }
        });
    }

    protected abstract Long dohLen(String finalKey);

    @Override
    public void hMSet(CacheKey key, final Map<String, String> m) {
        execute(key, CommandType.HASH, Protocol.Command.HMSET, new Executor<Object>() {
            public Object execute(String finalKey) {
                dohMSet(finalKey, m);
                return null;
            }
        });
    }

    protected abstract void dohMSet(String finalKey, Map<String, String> m);


    @Override
    public void hSet(CacheKey key, final String hashKey, final String value) {
        execute(key, CommandType.HASH, Protocol.Command.HSET, new Executor<Object>() {
            public Object execute(String finalKey) {
                dohSet(finalKey, hashKey, value);
                return null;
            }
        });
    }

    protected abstract void dohSet(String finalKey, String hashKey, String value);

    @Override
    public Boolean hSetNX(CacheKey key, final String hashKey, final String value) {
        return execute(key, CommandType.HASH, Protocol.Command.HSETNX, new Executor<Boolean>() {
            public Boolean execute(String finalKey) {
                return dohSetNX(finalKey, hashKey, value);
            }
        });
    }

    protected abstract Boolean dohSetNX(String finalKey, String hashKey, String value);

    @Override
    public List<String> hVals(CacheKey key) {
        return execute(key, CommandType.HASH, Protocol.Command.HVALS, new Executor<List<String>>() {
            public List<String> execute(String finalKey) {
                return dohVals(finalKey);
            }
        });
    }

    protected abstract List<String> dohVals(String finalKey);

    @Override
    public Map<String, String> hGetAll(CacheKey key) {
        return execute(key, CommandType.HASH, Protocol.Command.HGETALL, new Executor<Map<String, String>>() {
            public Map<String, String> execute(String finalKey) {
                return dohGetAll(finalKey);
            }
        });
    }

    protected abstract Map<String, String> dohGetAll(String finalKey);


    @Override
    public Boolean exists(CacheKey key) {
        return execute(key, CommandType.KEY, Protocol.Command.EXISTS, new Executor<Boolean>() {
            public Boolean execute(String finalKey) {
                return doExists(finalKey);
            }
        });
    }

    protected abstract Boolean doExists(String finalKey);

    @Override
    public void del(final CacheKey... keys) {
        execute(null, false, CommandType.KEY, Protocol.Command.DEL, new Executor<Object>() {
            public Object execute(String finalKey) {
                Collection<String> rawKeys = rawKeys(keys);
                doDel(rawKeys);
                return null;
            }
        });
    }


    protected abstract void doDel(Collection<String> finalKeys);

    @Override
    public String type(CacheKey key) {
        return execute(key, CommandType.KEY, Protocol.Command.TYPE, new Executor<String>() {
            public String execute(String finalKey) {
                return doType(finalKey);
            }
        });
    }

    protected abstract String doType(String finalKey);

    @Override
    public void rename(final CacheKey oldName, final CacheKey newName) {
        execute(null, false, CommandType.KEY, Protocol.Command.RENAME, new Executor<Object>() {
            public Object execute(String finalKey) {
                String oldKey = rawKey(oldName);
                String newKey = rawKey(newName);
                doReName(oldKey, newKey);
                return null;
            }
        });
    }

    protected abstract void doReName(String oldKey, String newKey);

    @Override
    public Boolean renameNX(final CacheKey oldName, final CacheKey newName) {
        return execute(null, false, CommandType.KEY, Protocol.Command.RENAME, new Executor<Boolean>() {
            public Boolean execute(String finalKey) {
                String oldKey = rawKey(oldName);
                String newKey = rawKey(newName);
                return doReNameNX(oldKey, newKey);
            }
        });
    }

    protected abstract Boolean doReNameNX(String oldName, String newName);

    @Override
    public Boolean expire(CacheKey key, final long timeout, final TimeUnit unit) {
        return execute(key, CommandType.KEY, Protocol.Command.EXPIRE, new Executor<Boolean>() {
            public Boolean execute(String finalKey) {
                return doExpire(finalKey, timeout, unit);
            }
        });
    }

    protected abstract Boolean doExpire(String finalKey, long timeout, TimeUnit unit);

    @Override
    public Boolean expireAt(CacheKey key, final Date date) {
        return execute(key, CommandType.KEY, Protocol.Command.EXPIREAT, new Executor<Boolean>() {
            public Boolean execute(String finalKey) {
                return doExpireAt(finalKey, date);
            }
        });
    }

    protected abstract Boolean doExpireAt(String finalKey, Date date);


    @Override
    public Boolean persist(CacheKey key) {
        return execute(key, CommandType.KEY, Protocol.Command.PERSIST, new Executor<Boolean>() {
            public Boolean execute(String finalKey) {
                return doPersist(finalKey);
            }
        });
    }

    protected abstract Boolean doPersist(String finalKey);


    @Override
    public List<String> lRange(CacheKey key, final long start, final long end) {
        return execute(key, CommandType.LIST, Protocol.Command.LRANGE, new Executor<List<String>>() {
            public List<String> execute(String finalKey) {
                return dolRang(finalKey, start, end);
            }
        });
    }

    protected abstract List<String> dolRang(String finalKey, long start, long end);

    @Override
    public void lTrim(CacheKey key, final long start, final long end) {
        execute(key, CommandType.LIST, Protocol.Command.LTRIM, new Executor<Object>() {
            public Object execute(String finalKey) {
                dolTrim(finalKey, start, end);
                return null;
            }
        });
    }

    protected abstract void dolTrim(String finalKey, long start, long end);

    @Override
    public Long lLen(CacheKey key) {
        return execute(key, CommandType.LIST, Protocol.Command.LLEN, new Executor<Long>() {
            public Long execute(String finalKey) {
                return dolLen(finalKey);
            }
        });
    }

    protected abstract Long dolLen(String finalKey);

    @Override
    public Long lPush(CacheKey key, final String... values) {
        return execute(key, CommandType.LIST, Protocol.Command.LPUSH, new Executor<Long>() {
            public Long execute(String finalKey) {
                return dolPush(finalKey, values);
            }
        });
    }

    protected abstract Long dolPush(String finalKey, String[] values);

    @Override
    public Long lPushX(CacheKey key, final String value) {
        return execute(key, CommandType.LIST, Protocol.Command.LPUSHX, new Executor<Long>() {
            public Long execute(String finalKey) {
                return dolPushX(finalKey, value);
            }
        });
    }

    protected abstract Long dolPushX(String finalKey, String value);

    @Override
    public Long lInsert(CacheKey key, final RedisListCommands.Position position, final String pivot, final String value) {
        return execute(key, CommandType.LIST, Protocol.Command.LINSERT, new Executor<Long>() {
            public Long execute(String finalKey) {
                return dolInsert(finalKey, position, pivot, value);
            }
        });
    }

    protected abstract Long dolInsert(String finalKey, RedisListCommands.Position position, String pivot, String value);

    @Override
    public Long rPush(CacheKey key, final String... values) {
        return execute(key, CommandType.LIST, Protocol.Command.RPUSH, new Executor<Long>() {
            public Long execute(String finalKey) {
                return dorPush(finalKey, values);
            }
        });
    }

    protected abstract Long dorPush(String finalKey, String[] values);

    @Override
    public Long rPushX(CacheKey key, final String value) {
        return execute(key, CommandType.LIST, Protocol.Command.RPUSHX, new Executor<Long>() {
            public Long execute(String finalKey) {
                return dorPushX(finalKey, value);
            }
        });
    }

    protected abstract Long dorPushX(String finalKey, String value);

    @Override
    public void lSet(CacheKey key, final long index, final String value) {
        execute(key, CommandType.LIST, Protocol.Command.LSET, new Executor<Object>() {
            public Object execute(String finalKey) {
                dolSet(finalKey, index, value);
                return null;
            }
        });

    }

    protected abstract void dolSet(String finalKey, long index, String value);

    @Override
    public Long lRem(CacheKey key, final long count, final String value) {
        return execute(key, CommandType.LIST, Protocol.Command.LREM, new Executor<Long>() {
            public Long execute(String finalKey) {
                return dolRem(finalKey, count, value);
            }
        });
    }

    protected abstract Long dolRem(String finalKey, long count, String value);

    @Override
    public String lIndex(CacheKey key, final long index) {
        return execute(key, CommandType.LIST, Protocol.Command.LINDEX, new Executor<String>() {
            public String execute(String finalKey) {
                return dolIndex(finalKey, index);
            }
        });
    }

    protected abstract String dolIndex(String finalKey, long index);

    @Override
    public String lPop(CacheKey key) {
        return execute(key, CommandType.LIST, Protocol.Command.LPOP, new Executor<String>() {
            public String execute(String finalKey) {
                return dolPop(finalKey);
            }
        });
    }

    protected abstract String dolPop(String finalKey);

    @Override
    public String bLPop(CacheKey key, final long timeout, final TimeUnit unit) {
        return execute(key, CommandType.LIST, Protocol.Command.BLPOP, new Executor<String>() {
            public String execute(String finalKey) {
                return dobLPop(finalKey, timeout, unit);
            }
        });
    }

    protected abstract String dobLPop(String finalKey, long timeout, TimeUnit unit);

    @Override
    public String rPop(CacheKey key) {
        return execute(key, CommandType.LIST, Protocol.Command.RPOP, new Executor<String>() {
            public String execute(String finalKey) {
                return dorPop(finalKey);
            }
        });
    }

    protected abstract String dorPop(String finalKey);

    @Override
    public String bRPop(CacheKey key, final long timeout, final TimeUnit unit) {
        return execute(key, CommandType.LIST, Protocol.Command.BRPOP, new Executor<String>() {
            public String execute(String finalKey) {
                return dobRPop(finalKey, timeout, unit);
            }
        });
    }

    protected abstract String dobRPop(String finalKey, long timeout, TimeUnit unit);

    @Override
    public String rPopLPush(final CacheKey sourceKey, final CacheKey destinationKey) {
        return execute(null, false, CommandType.LIST, Protocol.Command.RPOPLPUSH, new Executor<String>() {
            public String execute(String finalKey) {
                String sourceRawKey = rawKey(sourceKey);
                String destinationRawKey = rawKey(destinationKey);
                return dorPopLPush(sourceRawKey, destinationRawKey);
            }
        });
    }

    protected abstract String dorPopLPush(String sourceRawKey, String destinationRawKey);

    @Override
    public String bRPopLPush(final CacheKey sourceKey, final CacheKey destinationKey, final long timeout, final TimeUnit unit) {
        return execute(null, false, CommandType.LIST, Protocol.Command.BRPOPLPUSH, new Executor<String>() {
            public String execute(String finalKey) {
                String sourceRawKey = rawKey(sourceKey);
                String destinationRawKey = rawKey(destinationKey);
                return dobRPopLPush(sourceRawKey, destinationRawKey, timeout, unit);
            }
        });
    }

    protected abstract String dobRPopLPush(String sourceRawKey, String destinationRawKey, long timeout, TimeUnit unit);

    @Override
    public Long sAdd(CacheKey key, final String... values) {
        return execute(key, CommandType.SET, Protocol.Command.SADD, new Executor<Long>() {
            public Long execute(String finalKey) {
                return dosAdd(finalKey, values);
            }
        });
    }

    protected abstract Long dosAdd(String finalKey, String[] values);

    @Override
    public Long sRem(CacheKey key, final String... values) {
        return execute(key, CommandType.SET, Protocol.Command.SREM, new Executor<Long>() {
            public Long execute(String finalKey) {
                return dosRem(finalKey, values);
            }
        });
    }

    protected abstract Long dosRem(String finalKey, String[] values);

    @Override
    public String sPop(CacheKey key) {
        return execute(key, CommandType.SET, Protocol.Command.SPOP, new Executor<String>() {
            public String execute(String finalKey) {
                return dosPop(finalKey);
            }
        });
    }

    protected abstract String dosPop(String finalKey);

    @Override
    public Boolean sMove(final CacheKey key, final String value, final CacheKey destKey) {
        return execute(null, false, CommandType.SET, Protocol.Command.SMOVE, new Executor<Boolean>() {
            public Boolean execute(String finalKey) {
                String sourceRawKey = rawKey(key);
                String destRawKey = rawKey(destKey);
                return dosMove(sourceRawKey, value, destRawKey);
            }
        });
    }

    protected abstract Boolean dosMove(String sourceRawKey, String value, String destKey);

    @Override
    public Long sCard(CacheKey key) {
        return execute(key, CommandType.SET, Protocol.Command.SCARD, new Executor<Long>() {
            public Long execute(String finalKey) {
                return dosCard(finalKey);
            }
        });
    }

    protected abstract Long dosCard(String finalKey);

    @Override
    public Boolean sIsMember(CacheKey key, final String o) {
        return execute(key, CommandType.SET, Protocol.Command.SISMEMBER, new Executor<Boolean>() {
            public Boolean execute(String finalKey) {
                return dosIsMember(finalKey, o);
            }
        });
    }

    protected abstract Boolean dosIsMember(String finalKey, String o);

    @Override
    public Set<String> sInter(final CacheKey key, final Collection<CacheKey> otherKeys) {
        return execute(key, CommandType.SET, Protocol.Command.SINTER, new Executor<Set<String>>() {
            public Set<String> execute(String finalKey) {
                Collection<String> otherRawKeys = rawKeys(otherKeys);
                return dosInter(finalKey, otherRawKeys);
            }
        });
    }

    protected abstract Set<String> dosInter(String rawKey, Collection<String> otherRawKeys);

    @Override
    public Long sInterStore(final CacheKey key, final Collection<CacheKey> otherKeys, final CacheKey destKey) {
        return execute(key, CommandType.SET, Protocol.Command.SINTERSTORE, new Executor<Long>() {
            public Long execute(String finalKey) {
                Collection<String> otherRawKeys = rawKeys(otherKeys);
                String destRawKey = rawKey(destKey);
                return dosInterStore(finalKey, otherRawKeys, destRawKey);
            }
        });
    }

    protected abstract Long dosInterStore(String rawKey, Collection<String> otherRawKeys, String destRawKey);

    @Override
    public Set<String> sUnion(final CacheKey key, final Collection<CacheKey> otherKeys) {
        return execute(key, CommandType.SET, Protocol.Command.SUNION, new Executor<Set<String>>() {
            public Set<String> execute(String finalKey) {
                Collection<String> otherRawKeys = rawKeys(otherKeys);
                return dosUnion(finalKey, otherRawKeys);
            }
        });
    }

    protected abstract Set<String> dosUnion(String rawKey, Collection<String> otherRawKeys);

    @Override
    public Long sUnionStore(final CacheKey key, final Collection<CacheKey> otherKeys, final CacheKey destKey) {
        return execute(key, CommandType.SET, Protocol.Command.SUNIONSTORE, new Executor<Long>() {
            public Long execute(String finalKey) {
                Collection<String> otherRawKeys = rawKeys(otherKeys);
                String destRawKey = rawKey(destKey);
                return dosUnionStore(finalKey, otherRawKeys, destRawKey);
            }
        });
    }

    protected abstract Long dosUnionStore(String rawKey, Collection<String> otherRawKeys, String destRawKey);

    @Override
    public Set<String> sDiff(final CacheKey key, final Collection<CacheKey> otherKeys) {
        return execute(key, CommandType.SET, Protocol.Command.SDIFF, new Executor<Set<String>>() {
            public Set<String> execute(String finalKey) {
                Collection<String> otherRawKeys = rawKeys(otherKeys);
                return dosDiff(finalKey, otherRawKeys);
            }
        });
    }

    protected abstract Set<String> dosDiff(String rawKey, Collection<String> otherRawKeys);

    @Override
    public Long sDiffStore(CacheKey key, final Collection<CacheKey> otherKeys, final CacheKey destKey) {
        return execute(key, CommandType.SET, Protocol.Command.SDIFFSTORE, new Executor<Long>() {
            public Long execute(String finalKey) {
                Collection<String> otherRawKeys = rawKeys(otherKeys);
                String destRawKey = rawKey(destKey);
                return dosDiffStore(finalKey, otherRawKeys, destRawKey);
            }
        });
    }

    protected abstract Long dosDiffStore(String finalKey, Collection<String> otherRawKeys, String destRawKey);

    @Override
    public Set<String> sMembers(CacheKey key) {
        return execute(key, CommandType.SET, Protocol.Command.SMEMBERS, new Executor<Set<String>>() {
            public Set<String> execute(String finalKey) {
                return dosMembers(finalKey);
            }
        });
    }

    protected abstract Set<String> dosMembers(String finalKey);

    @Override
    public String sRandomMember(CacheKey key) {
        return execute(key, CommandType.SET, Protocol.Command.SRANDMEMBER, new Executor<String>() {
            public String execute(String finalKey) {
                return dosRandomMember(finalKey);
            }
        });
    }

    protected abstract String dosRandomMember(String finalKey);

    @Override
    public List<String> sRandomMember(CacheKey key, final long count) {
        return execute(key, CommandType.SET, Protocol.Command.SRANDMEMBER, new Executor<List<String>>() {
            public List<String> execute(String finalKey) {
                return dosRandomMember(finalKey, count);
            }
        });
    }

    protected abstract List<String> dosRandomMember(String finalKey, long count);

    @Override
    public Boolean zAdd(CacheKey key, final String value, final double score) {
        return execute(key, CommandType.ZSET, Protocol.Command.ZADD, new Executor<Boolean>() {
            public Boolean execute(String finalKey) {
                return dozAdd(finalKey, value, score);
            }
        });
    }

    protected abstract Boolean dozAdd(String finalKey, String value, double score);

    @Override
    public Long zAdd(CacheKey key, final Set<ZSetOperations.TypedTuple<String>> tuples) {
        return execute(key, CommandType.ZSET, Protocol.Command.ZADD, new Executor<Long>() {
            public Long execute(String finalKey) {
                return dozAdd(finalKey, tuples);
            }
        });
    }

    protected abstract Long dozAdd(String finalKey, Set<ZSetOperations.TypedTuple<String>> tuples);

    @Override
    public Long zRem(CacheKey key, final String... values) {
        return execute(key, CommandType.ZSET, Protocol.Command.ZREM, new Executor<Long>() {
            public Long execute(String finalKey) {
                return dozRem(finalKey, values);
            }
        });
    }

    protected abstract Long dozRem(String finalKey, String[] values);

    @Override
    public Double zIncrBy(CacheKey key, final String value, final double delta) {
        return execute(key, CommandType.ZSET, Protocol.Command.ZINCRBY, new Executor<Double>() {
            public Double execute(String finalKey) {
                return dozIncrBy(finalKey, value, delta);
            }
        });
    }

    protected abstract Double dozIncrBy(String finalKey, String value, double delta);

    @Override
    public Long zRank(CacheKey key, final String o) {
        return execute(key, CommandType.ZSET, Protocol.Command.ZRANK, new Executor<Long>() {
            public Long execute(String finalKey) {
                return dozRank(finalKey, o);
            }
        });
    }

    protected abstract Long dozRank(String finalKey, String o);

    @Override
    public Long zRevRank(CacheKey key, final String o) {
        return execute(key, CommandType.ZSET, Protocol.Command.ZREVRANK, new Executor<Long>() {
            public Long execute(String finalKey) {
                return dozRevRank(finalKey, o);
            }
        });
    }

    protected abstract Long dozRevRank(String finalKey, String o);

    @Override
    public Set<String> zRange(CacheKey key, final long start, final long end) {
        return execute(key, CommandType.ZSET, Protocol.Command.ZRANGE, new Executor<Set<String>>() {
            public Set<String> execute(String finalKey) {
                return dozRange(finalKey, start, end);
            }
        });
    }

    protected abstract Set<String> dozRange(String finalKey, long start, long end);

    @Override
    public Set<ZSetOperations.TypedTuple<String>> zRangeWithScores(CacheKey key, final long start, final long end) {
        return execute(key, CommandType.ZSET, Protocol.Command.ZRANGE, new Executor<Set<ZSetOperations.TypedTuple<String>>>() {
            public Set<ZSetOperations.TypedTuple<String>> execute(String finalKey) {
                return dozRangeWithScores(finalKey, start, end);
            }
        });
    }

    protected abstract Set<ZSetOperations.TypedTuple<String>> dozRangeWithScores(String finalKey, long start, long end);

    @Override
    public Set<String> zRangeByScore(CacheKey key, final double min, final double max) {
        return execute(key, CommandType.ZSET, Protocol.Command.ZRANGEBYSCORE, new Executor<Set<String>>() {
            public Set<String> execute(String finalKey) {
                return dozRangeByScore(finalKey, min, max);
            }
        });
    }

    protected abstract Set<String> dozRangeByScore(String finalKey, double min, double max);

    @Override
    public Set<ZSetOperations.TypedTuple<String>> zRangeByScoreWithScores(CacheKey key, final double min, final double max) {
        return execute(key, CommandType.ZSET, Protocol.Command.ZRANGEBYSCORE, new Executor<Set<ZSetOperations.TypedTuple<String>>>() {
            public Set<ZSetOperations.TypedTuple<String>> execute(String finalKey) {
                return dozRangeByScoreWithScores(finalKey, min, max);
            }
        });
    }

    protected abstract Set<ZSetOperations.TypedTuple<String>> dozRangeByScoreWithScores(String finalKey, double min, double max);

    @Override
    public Set<String> zRangeByScore(CacheKey key, final double min, final double max, final long offset, final long count) {
        return execute(key, CommandType.ZSET, Protocol.Command.ZRANGEBYSCORE, new Executor<Set<String>>() {
            public Set<String> execute(String finalKey) {
                return dozRangeByScore(finalKey, min, max, offset, count);
            }
        });
    }

    protected abstract Set<String> dozRangeByScore(String finalKey, double min, double max, long offset, long count);

    @Override
    public Set<ZSetOperations.TypedTuple<String>> zRangeByScoreWithScores(CacheKey key, final double min, final double max, final long offset, final long count) {
        return execute(key, CommandType.ZSET, Protocol.Command.ZRANGEBYSCORE, new Executor<Set<ZSetOperations.TypedTuple<String>>>() {
            public Set<ZSetOperations.TypedTuple<String>> execute(String finalKey) {
                return dozRangeByScoreWithScores(finalKey, min, max, offset, count);
            }
        });
    }

    protected abstract Set<ZSetOperations.TypedTuple<String>> dozRangeByScoreWithScores(String finalKey, double min, double max, long offset, long count);

    @Override
    public Set<String> zRevRange(CacheKey key, final long start, final long end) {
        return execute(key, CommandType.ZSET, Protocol.Command.ZREVRANGE, new Executor<Set<String>>() {
            public Set<String> execute(String finalKey) {
                return dozRevRange(finalKey, start, end);
            }
        });
    }

    protected abstract Set<String> dozRevRange(String finalKey, long start, long end);

    @Override
    public Set<ZSetOperations.TypedTuple<String>> zRevRangeWithScores(CacheKey key, final long start, final long end) {
        return execute(key, CommandType.ZSET, Protocol.Command.ZREVRANGE, new Executor<Set<ZSetOperations.TypedTuple<String>>>() {
            public Set<ZSetOperations.TypedTuple<String>> execute(String finalKey) {
                return dozRevRangeWithScores(finalKey, start, end);
            }
        });
    }

    protected abstract Set<ZSetOperations.TypedTuple<String>> dozRevRangeWithScores(String finalKey, long start, long end);

    @Override
    public Set<String> zRevRangeByScore(CacheKey key, final double min, final double max) {
        return execute(key, CommandType.ZSET, Protocol.Command.ZREVRANGEBYSCORE, new Executor<Set<String>>() {
            public Set<String> execute(String finalKey) {
                return dozRevRangeByScore(finalKey, min, max);
            }
        });
    }

    protected abstract Set<String> dozRevRangeByScore(String finalKey, double min, double max);

    @Override
    public Set<ZSetOperations.TypedTuple<String>> zRevRangeByScoreWithScores(CacheKey key, final double min, final double max) {
        return execute(key, CommandType.ZSET, Protocol.Command.ZREVRANGEBYSCORE, new Executor<Set<ZSetOperations.TypedTuple<String>>>() {
            public Set<ZSetOperations.TypedTuple<String>> execute(String finalKey) {
                return dozRevRangeByScoreWithScores(finalKey, min, max);
            }
        });
    }

    protected abstract Set<ZSetOperations.TypedTuple<String>> dozRevRangeByScoreWithScores(String finalKey, double min, double max);

    @Override
    public Set<String> zRevRangeByScore(CacheKey key, final double min, final double max, final long offset, final long count) {
        return execute(key, CommandType.ZSET, Protocol.Command.ZREVRANGEBYSCORE, new Executor<Set<String>>() {
            public Set<String> execute(String finalKey) {
                return dozRevRangeByScore(finalKey, min, max, offset, count);
            }
        });
    }

    protected abstract Set<String> dozRevRangeByScore(String finalKey, double min, double max, long offset, long count);

    @Override
    public Set<ZSetOperations.TypedTuple<String>> zRevRangeByScoreWithScores(CacheKey key, final double min, final double max, final long offset, final long count) {
        return execute(key, CommandType.ZSET, Protocol.Command.ZREVRANGEBYSCORE, new Executor<Set<ZSetOperations.TypedTuple<String>>>() {
            public Set<ZSetOperations.TypedTuple<String>> execute(String finalKey) {
                return dozRevRangeByScoreWithScores(finalKey, min, max, offset, count);
            }
        });
    }

    protected abstract Set<ZSetOperations.TypedTuple<String>> dozRevRangeByScoreWithScores(String finalKey, double min, double max, long offset, long count);

    @Override
    public Long zCount(CacheKey key, final double min, final double max) {
        return execute(key, CommandType.ZSET, Protocol.Command.ZCOUNT, new Executor<Long>() {
            public Long execute(String finalKey) {
                return dozCount(finalKey, min, max);
            }
        });
    }

    protected abstract Long dozCount(String finalKey, double min, double max);

    @Override
    public Long zCard(CacheKey key) {
        return execute(key, CommandType.ZSET, Protocol.Command.ZCARD, new Executor<Long>() {
            public Long execute(String finalKey) {
                return dozCard(finalKey);
            }
        });
    }

    protected abstract Long dozCard(String finalKey);

    @Override
    public Double zScore(CacheKey key, final String o) {
        return execute(key, CommandType.ZSET, Protocol.Command.ZSCORE, new Executor<Double>() {
            public Double execute(String finalKey) {
                return dozScore(finalKey, o);
            }
        });
    }

    protected abstract Double dozScore(String finalKey, String o);

    @Override
    public Long zRemRangeByRank(CacheKey key, final long start, final long end) {
        return execute(key, CommandType.ZSET, Protocol.Command.ZREMRANGEBYRANK, new Executor<Long>() {
            public Long execute(String finalKey) {
                return dozRemRangeByRank(finalKey, start, end);
            }
        });
    }

    protected abstract Long dozRemRangeByRank(String finalKey, long start, long end);

    @Override
    public Long zRemRangeByScore(CacheKey key, final double min, final double max) {
        return execute(key, CommandType.ZSET, Protocol.Command.ZREMRANGEBYSCORE, new Executor<Long>() {
            public Long execute(String finalKey) {
                return dozRemRangeByScore(finalKey, min, max);
            }
        });
    }

    protected abstract Long dozRemRangeByScore(String finalKey, double min, double max);

    @Override
    public Long zUnionStore(CacheKey key, final Collection<CacheKey> otherKeys, final CacheKey destKey) {
        return execute(key, CommandType.ZSET, Protocol.Command.ZUNIONSTORE, new Executor<Long>() {
            @Override
            public Long execute(String finalKey) {
                Collection<String> otherRawKeys = rawKeys(otherKeys);
                String destRawKey = rawKey(destKey);
                return dozUnionStore(finalKey, otherRawKeys, destRawKey);
            }
        });
    }

    protected abstract Long dozUnionStore(String finalKey, Collection<String> otherRawKeys, String destRawKey);

    @Override
    public Long zInterStore(CacheKey key, final Collection<CacheKey> otherKeys, final CacheKey destKey) {
        return execute(key, CommandType.ZSET, Protocol.Command.ZINTERSTORE, new Executor<Long>() {
            public Long execute(String finalKey) {
                Collection<String> otherRawKeys = rawKeys(otherKeys);
                String destRawKey = rawKey(destKey);
                return dozInterStore(finalKey, otherRawKeys, destRawKey);
            }
        });
    }

    protected abstract Long dozInterStore(String finalKey, Collection<String> otherRawKeys, String destRawKey);

    @Override
    public Set<String> zRangeByLex(CacheKey key, final RedisZSetCommands.Range range) {
        return execute(key, CommandType.ZSET, Protocol.Command.ZRANGEBYLEX, new Executor<Set<String>>() {
            public Set<String> execute(String finalKey) {
                return dozRangeByLex(finalKey, range);
            }
        });
    }

    protected abstract Set<String> dozRangeByLex(String finalKey, RedisZSetCommands.Range range);

    @Override
    public Set<String> zRangeByLex(CacheKey key, final RedisZSetCommands.Range range, final RedisZSetCommands.Limit limit) {
        return execute(key, CommandType.ZSET, Protocol.Command.ZRANGEBYLEX, new Executor<Set<String>>() {
            public Set<String> execute(String finalKey) {
                return dozRangeByLex(finalKey, range, limit);
            }
        });
    }

    protected abstract Set<String> dozRangeByLex(String finalKey, RedisZSetCommands.Range range, RedisZSetCommands.Limit limit);
}
