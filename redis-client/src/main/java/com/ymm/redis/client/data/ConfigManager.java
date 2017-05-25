package com.ymm.redis.client.data;

import com.ymm.redis.common.dto.Cluster;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.util.Assert;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantLock;

public class ConfigManager {

    private Set<RedisNode> redisNodes;

    private Cluster cluster;

    private ConcurrentMap<String, String> itemsMap = new ConcurrentHashMap<>();

    private ReentrantLock lock = new ReentrantLock();

    private ConfigManager() {
    }

    public static ConfigManager getInstance() {
        return ConfigManagerHolder.INSTANCE;
    }

    private static class ConfigManagerHolder {
        private static final ConfigManager INSTANCE = new ConfigManager();
    }

    public String getPassword() {
        this.lock.lock();
        try {
            return this.cluster.getPassword();
        } finally {
            this.lock.unlock();
        }
    }

    public String getItem(String itemKey) {
        return itemsMap.get(itemKey);
    }

    public void setItems(Map<String, String> map) {
        if (map != null && !map.isEmpty()) {
            Set<Map.Entry<String, String>> entrySet = map.entrySet();
            Set<String> currentItemKeys = new HashSet<>();
            for (Map.Entry<String, String> entry : entrySet) {
                currentItemKeys.add(entry.getKey());
                itemsMap.put(entry.getKey(), entry.getValue());
            }
            String[] existsItemKeys = itemsMap.keySet().toArray(new String[0]);
            Set<String> existsItemKeySet = new HashSet<>(existsItemKeys.length);
            Collections.addAll(existsItemKeySet, existsItemKeys);
            if (existsItemKeySet.size() > 0 && currentItemKeys.size() > 0) {
                existsItemKeySet.removeAll(currentItemKeys);
                if (existsItemKeySet.size() > 0) {
                    for (String itemKey : existsItemKeySet) {
                        itemsMap.remove(itemKey);
                    }
                }
            }
        }
    }

    public Set<RedisNode> getRedisNodes() {
        this.lock.lock();
        try {
            return this.redisNodes;
        } finally {
            this.lock.unlock();
        }
    }

    public boolean setCluster(Cluster newCluster) {
        this.lock.lock();
        try {

            Assert.notNull(newCluster, "cluster must not be null");
            Assert.hasText(newCluster.getHostAndPorts(), "hostAndPorts must not be null");

            boolean isChanged = false;

            if (this.cluster != null) {
                //change
                if (!this.cluster.equals(newCluster)) {
                    this.cluster = newCluster;
                    isChanged = true;
                }
            } else {
                //init
                this.cluster = newCluster;
            }

            String[] hostAndPortArr = newCluster.getHostAndPorts().split(",");
            this.redisNodes = new LinkedHashSet<>();
            for (String hostAndPort : hostAndPortArr) {
                RedisNode redisNode = readHostAndPortFromString(hostAndPort);
                this.redisNodes.add(redisNode);
            }
            return isChanged;
        } finally {
            this.lock.unlock();
        }
    }

    private RedisNode readHostAndPortFromString(String hostAndPort) {
        String[] args = StringUtils.split(hostAndPort, ":");
        Assert.notNull(args, "HostAndPort need to be seperated by  \':\'.");
        Assert.isTrue(args.length == 2, "Host and Port String needs to specified as host:port");
        return new RedisNode(args[0], Integer.valueOf(args[1]));
    }

}
