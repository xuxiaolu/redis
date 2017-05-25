package com.xuxl.redis.admin;

import com.xuxl.redis.admin.service.BusinessLineService;
import com.xuxl.redis.admin.service.ClusterService;
import com.xuxl.redis.admin.service.ItemService;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ClusterConfigSyncListener implements ApplicationListener<ContextRefreshedEvent> {

    @Resource
    private ClusterService clusterService;

    @Resource
    private BusinessLineService businessLineService;

    @Resource
    private ItemService itemService;

    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            ClusterConfigSyncThread clusterConfigSyncThread = new ClusterConfigSyncThread(clusterService, businessLineService, itemService);
            Thread thread = new Thread(clusterConfigSyncThread, "cluster-sync-config-thread");
            thread.setDaemon(true);
            thread.start();
        }
    }
}
