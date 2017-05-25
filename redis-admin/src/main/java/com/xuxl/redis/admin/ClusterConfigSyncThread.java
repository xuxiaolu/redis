package com.xuxl.redis.admin;


import com.xuxl.redis.admin.service.BusinessLineService;
import com.xuxl.redis.admin.entity.BusinessLine;
import com.xuxl.redis.admin.entity.Item;
import com.xuxl.redis.admin.service.ClusterService;
import com.xuxl.redis.admin.service.ItemService;
import com.xuxl.redis.admin.zookeeper.ZkClient;
import com.xuxl.redis.common.Constants;
import com.xuxl.redis.common.JsonUtils;
import com.xuxl.redis.common.dto.Cluster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ClusterConfigSyncThread implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClusterConfigSyncThread.class);

    private static final long DEFAULT_SYNC_INTERVAL = 5 * 60 * 1000;

    private long latestSyncTime = System.currentTimeMillis();

    private AtomicBoolean isSyncing = new AtomicBoolean(false);

    private ZkClient zkClient = ZkClient.getInstance();

    private ClusterService clusterService;

    private BusinessLineService businessLineService;

    private ItemService itemService;

    ClusterConfigSyncThread(ClusterService clusterService, BusinessLineService businessLineService, ItemService itemService) {
        this.clusterService = clusterService;
        this.businessLineService = businessLineService;
        this.itemService = itemService;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            long now = System.currentTimeMillis();
            if (now - latestSyncTime >= DEFAULT_SYNC_INTERVAL) {
                if (this.isSyncing.compareAndSet(false, true)) {
                    try {
                        LOGGER.debug("sync config begin");
                        syncAvailableClusters();
                        syncUnAvailableClusters();
                        LOGGER.debug("sync config end");
                    } catch (Exception e) {
                        LOGGER.error("sync config has error", e);
                    } finally {
                        latestSyncTime = now;
                        this.isSyncing.set(false);
                    }
                }
            }
        }
    }

    /**
     * 同步可用集群信息
     * 1.同步集群信息，path存在，更新数据；path不存在，插入数据
     * 2.同步item信息，
     * a)item可用: itemPath存在，更新数据;itemPath不存在，插入数据
     * b)item不可用: itemPath存在，删除itemPath
     */
    private void syncAvailableClusters() {
        List<com.xuxl.redis.admin.entity.Cluster> availableClusters = clusterService.getAllAvailableClusters();
        if (availableClusters != null && availableClusters.size() > 0) {
            for (com.xuxl.redis.admin.entity.Cluster cluster : availableClusters) {
                String clusterName = cluster.getName();
                Cluster newCluster = new Cluster();
                newCluster.setHostAndPorts(cluster.getHostAndPorts());
                newCluster.setPassword(cluster.getPassword());
                String newData = JsonUtils.convertJson(newCluster);
                String clusterPath = Constants.getRealClusterPath(clusterName);
                //a.同步集群信息
                boolean clusterExists = zkClient.exists(clusterPath, false);
                if (clusterExists) {
                    String oldData = zkClient.getData(clusterPath, false);
                    Cluster oldCluster = JsonUtils.convertObject(oldData, Cluster.class);
                    if (!newCluster.equals(oldCluster)) {
                        zkClient.setData(clusterPath, newData);
                    }
                } else {
                    zkClient.create(clusterPath, newData, false);
                }

                //b.同步Item信息
                List<BusinessLine> businessLines = businessLineService.getBusinessLinesByClusterName(clusterName);
                if (businessLines != null && businessLines.size() > 0) {
                    for (BusinessLine businessLine : businessLines) {
                        List<Item> availableItems = itemService.getAvailableItemsByBizName(businessLine.getName());
                        if (availableItems != null && availableItems.size() > 0) {
                            for (Item item : availableItems) {
                                String name = item.getName();
                                String newValue = item.getValue();
                                String itemPath = Constants.getRealItemPath(businessLine.getClusterName(), businessLine.getName(), name);
                                boolean itemExists = zkClient.exists(itemPath, false);
                                if (itemExists) {
                                    String oldValue = zkClient.getData(itemPath, false);
                                    if (!newValue.equals(oldValue)) {
                                        zkClient.setData(itemPath, newValue);
                                    }
                                } else {
                                    zkClient.create(itemPath, newValue, false);
                                }
                            }
                        }

                        List<Item> unAvailableItems = itemService.getUnAvailableItemsByBizName(businessLine.getName());
                        if (unAvailableItems != null && unAvailableItems.size() > 0) {
                            for (Item item : unAvailableItems) {
                                String name = item.getName();
                                String itemPath = Constants.getRealItemPath(businessLine.getClusterName(), businessLine.getName(), name);
                                boolean itemExists = zkClient.exists(itemPath, false);
                                if (itemExists) {
                                    zkClient.delete(itemPath);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 同步不可用集群（删除）
     * 1.同步不可用集群，path存在，删除path
     * 2.删除Item，itemPath存在，删除itemPath
     */
    private void syncUnAvailableClusters() {
        List<com.xuxl.redis.admin.entity.Cluster> unAvailableClusters = clusterService.getAllUnAvailableClusters();
        if (unAvailableClusters != null && unAvailableClusters.size() > 0) {
            for (com.xuxl.redis.admin.entity.Cluster cluster : unAvailableClusters) {
                String name = cluster.getName();
                String clusterPath = Constants.getRealClusterPath(name);
                boolean clusterExists = zkClient.exists(clusterPath, false);
                if (clusterExists) {
                    zkClient.delete(clusterPath);
                }

                List<BusinessLine> businessLines = businessLineService.getBusinessLinesByClusterName(name);
                if (businessLines != null && businessLines.size() > 0) {
                    for (BusinessLine businessLine : businessLines) {
                        List<Item> allItems = itemService.getAllItemsByBizName(businessLine.getName());
                        if (allItems != null && allItems.size() > 0) {
                            for (Item item : allItems) {
                                String itemPath = Constants.getRealItemPath(businessLine.getClusterName(), businessLine.getName(), item.getName());
                                boolean itemExists = zkClient.exists(itemPath, false);
                                if (itemExists) {
                                    zkClient.delete(itemPath);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
