package com.xuxl.redis.client.zookeeper;

public class ZkClientLifecycle {

    public void close() {
        ZkClient.getInstance().close();
    }
}
