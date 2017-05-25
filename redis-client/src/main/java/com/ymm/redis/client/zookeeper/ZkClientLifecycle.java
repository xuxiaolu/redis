package com.ymm.redis.client.zookeeper;

import com.ymm.redis.client.zookeeper.ZkClient;

public class ZkClientLifecycle {

    public void close() {
        ZkClient.getInstance().close();
    }
}
