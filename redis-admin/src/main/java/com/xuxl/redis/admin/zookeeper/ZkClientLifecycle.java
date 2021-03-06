package com.xuxl.redis.admin.zookeeper;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class ZkClientLifecycle {


    @PostConstruct
    public void init() {
        ZkClient.getInstance().init();
    }


    @PreDestroy
    public void destroy() {
        ZkClient.getInstance().close();
    }
}
