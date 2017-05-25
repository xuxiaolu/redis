package com.xuxl.redis.client.zookeeper;

import com.xuxl.redis.client.data.ClusterEnvironment;
import com.xuxl.redis.client.data.ConfigManager;
import com.xuxl.redis.client.exception.ClientException;
import com.xuxl.redis.client.listener.ClusterChangeListener;
import com.xuxl.redis.client.listener.ItemChangeListener;
import com.xuxl.redis.common.Constants;
import com.xuxl.redis.common.JsonUtils;
import com.xuxl.redis.common.dto.Cluster;
import com.xuxl.redis.common.zookeeper.ZkClients;
import org.apache.commons.lang.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.RetryNTimes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.util.Assert;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class ZkClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZkClient.class);

    private static final int DEFAULT_ZK_RETRY_INTERVAL = 1000;

    private static final long DEFAULT_SYNC_INTERVAL = 2 * 60 * 1000;

    private String zkAddress = "192.168.199.13:2181,192.168.199.14:2181,192.168.199.15:2181";

    private long lastSyncTime = System.currentTimeMillis();

    private AtomicBoolean isSyncing = new AtomicBoolean(false);

    private CuratorFramework curatorClient;

    private ZkClient() {
        init();
    }

    public static ZkClient getInstance() {
        return ZkClientHolder.INSTANCE;
    }

    private static class ZkClientHolder {
        private static final ZkClient INSTANCE = new ZkClient();
    }

    private void init() {
        Assert.hasText(zkAddress, "zkAddress is empty");
        this.curatorClient = newClient();
        this.startConfigSync();
    }

    void close() {
        if (this.curatorClient != null) {
            this.curatorClient.close();
            this.curatorClient = null;
        }
    }

    private CuratorFramework newClient() {
        CuratorFramework curatorClient = ZkClients.newClient(zkAddress, 60 * 1000, 30 * 1000,
                new RetryNTimes(Integer.MAX_VALUE, DEFAULT_ZK_RETRY_INTERVAL));

        curatorClient.getConnectionStateListenable().addListener(new ConnectionStateListener() {
            public void stateChanged(CuratorFramework curatorFramework, ConnectionState newState) {
                LOGGER.info(String.format("redis client zookeeper state changed to %s", newState));
                if (ConnectionState.RECONNECTED == newState) {
                    syncAll(true);
                }
            }
        });

        curatorClient.getCuratorListenable().addListener(new ZkListener());

        ZkClients.start(curatorClient);

        return curatorClient;
    }

    public Set<RedisNode> getRedisNodes() {
        syncAll(true);
        return ConfigManager.getInstance().getRedisNodes();
    }

    private boolean isZkConnected() {
        return this.curatorClient != null && this.curatorClient.getZookeeperClient().isConnected();
    }


    private void syncAll(boolean force) {
        long now = System.currentTimeMillis();
        if (force || now - this.lastSyncTime >= DEFAULT_SYNC_INTERVAL) {
            if (this.isSyncing.compareAndSet(false, true)) {
                try {
                    syncData();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                    LOGGER.error("failed to sync config", e);
                } finally {
                    this.lastSyncTime = now;
                    this.isSyncing.set(false);
                }
            }
        }
    }

    private void syncData() throws Exception {
        if (isZkConnected()) {
            try {
                syncCluster();
                syncItems();
            } catch (Exception e) {
                LOGGER.error("sync data has error", e);
                throw e;
            }
        }
    }

    private void syncCluster() throws Exception {
        String clusterName = ClusterEnvironment.getClusterName();
        String clusterPath = Constants.getRealClusterPath(clusterName);
        boolean exists = ZkClients.exists(curatorClient, clusterPath, true);
        if (exists) {
            String data = ZkClients.getData(curatorClient, clusterPath, true);
            if (StringUtils.isBlank(data)) {
                throw new ClientException(String.format("%s cluster has no data", clusterName));
            }
            Cluster cluster = JsonUtils.convertObject(data, Cluster.class);
            Assert.notNull(cluster, "cluster is null");
            ClusterChangeListener.handleCluster(cluster);
            return;
        }
        throw new ClientException(String.format("%s cluster is not exists", clusterName));
    }

    private void syncItems() {
        String clusterName = ClusterEnvironment.getClusterName();
        String businessLineName = ClusterEnvironment.getBusinessLineName();
        String itemParentPath = Constants.getRealParentItemPath(clusterName, businessLineName);
        List<String> itemPaths = ZkClients.getChildren(curatorClient, itemParentPath, true);
        if (itemPaths != null && itemPaths.size() > 0) {
            Map<String, String> map = new LinkedHashMap<>();
            for (String itemPath : itemPaths) {
                String data = ZkClients.getData(curatorClient, Constants.getRealItemPath(clusterName, businessLineName, itemPath), false);
                map.put(itemPath, data);
            }
            ItemChangeListener.handleItems(map);
            return;
        }
        LOGGER.warn("{} has no item", businessLineName);
    }

    private void startConfigSync() {
        Thread t = new Thread(new ConfigSync(), "cluster-config-sync");
        t.setDaemon(true);
        t.start();
    }

    class ConfigSync implements Runnable {

        public void run() {
            while (!Thread.interrupted()) {
                try {
                    Thread.sleep(1000);
                    ZkClient.this.syncAll(false);
                } catch (Exception e) {
                    LOGGER.warn("cluster config sync thread is interrupted", e);
                    break;
                }
            }
        }
    }

}
