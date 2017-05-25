package com.xuxl.redis.admin.zookeeper;


import com.xuxl.redis.common.zookeeper.ZkClients;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.RetryNTimes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.nio.charset.Charset;

public class ZkClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZkClient.class);

    private static final int DEFAULT_ZK_RETRY_INTERVAL = 1000;

    private String zkAddress = "192.168.199.13:2181,192.168.199.14:2181,192.168.199.15:2181";

    private CuratorFramework curatorClient;

    private ZkClient() {
    }

    public static ZkClient getInstance() {
        return ZkClientHolder.INSTANCE;
    }

    private static class ZkClientHolder {
        private static final ZkClient INSTANCE = new ZkClient();
    }

    public void init() {
        Assert.hasText(zkAddress, "zkAddress is empty");
        this.curatorClient = this.newClient();
    }

    public void close() {
        if (this.curatorClient != null) {
            this.curatorClient.close();
        }

    }

    public String getData(String path, boolean watched) {
        if (isZkConnected()) {
            return ZkClients.getData(this.curatorClient, path, watched);
        }
        return null;
    }

    public boolean exists(String path, boolean watched) {
        return isZkConnected() && ZkClients.exists(this.curatorClient, path, watched);
    }

    public void create(String path, String content, boolean watched) {
        if (isZkConnected()) {
            ZkClients.createPath(this.curatorClient, path, content.getBytes(Charset.forName("UTF-8")), watched);
        }
    }

    public void setData(String path, String content) {
        if (isZkConnected()) {
            ZkClients.setData(this.curatorClient, path, content.getBytes(Charset.forName("UTF-8")));
        }
    }

    public void delete(String path) {
        if (isZkConnected()) {
            ZkClients.delete(curatorClient, path);
        }
    }

    private boolean isZkConnected() {
        return this.curatorClient != null && this.curatorClient.getZookeeperClient().isConnected();
    }

    private CuratorFramework newClient() {
        CuratorFramework curatorClient = ZkClients.newClient(zkAddress, 60 * 1000, 30 * 1000,
                new RetryNTimes(Integer.MAX_VALUE, DEFAULT_ZK_RETRY_INTERVAL));
        curatorClient.getConnectionStateListenable().addListener(new ConnectionStateListener() {
            public void stateChanged(CuratorFramework client, ConnectionState newState) {
                LOGGER.info("redis admin zookeeper state changed to {}", newState);
            }
        });
        ZkClients.start(curatorClient);
        return curatorClient;
    }

}
