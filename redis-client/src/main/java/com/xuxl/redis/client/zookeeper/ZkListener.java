package com.xuxl.redis.client.zookeeper;

import com.xuxl.redis.client.data.ClusterEnvironment;
import com.xuxl.redis.client.listener.ClusterChangeListener;
import com.xuxl.redis.client.listener.ItemChangeListener;
import com.xuxl.redis.common.JsonUtils;
import com.xuxl.redis.common.dto.Cluster;
import com.xuxl.redis.common.Constants;
import com.xuxl.redis.common.zookeeper.ZkClients;
import org.apache.commons.lang.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorEventType;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ZkListener implements CuratorListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZkListener.class);

    private final Object clusterChangeLock = new Object();

    private final Object itemChangeLock = new Object();

    public void eventReceived(CuratorFramework curatorClient, CuratorEvent curatorEvent) throws Exception {
        if (curatorEvent == null) {
            LOGGER.warn("curator event is null");
            return;
        }

        if (CuratorEventType.WATCHED == curatorEvent.getType()) {
            WatchedEvent watchedEvent = curatorEvent.getWatchedEvent();
            if (watchedEvent == null) {
                LOGGER.warn("zk event is null");
                return;
            }

            Watcher.Event.EventType eventType = watchedEvent.getType();
            if (eventType == Watcher.Event.EventType.None) {
                return;
            }

            String path = watchedEvent.getPath();
            if (StringUtils.isBlank(path)) {
                return;
            }
            LOGGER.info("path:{} ,eventType:{} ", new Object[]{path, eventType.name()});

            if (eventType == Watcher.Event.EventType.NodeDataChanged || eventType == Watcher.Event.EventType.NodeCreated) {
                if (path.startsWith(Constants.REDIS_CLUSTER_PATH)) {
                    synchronized (clusterChangeLock) {
                        processClusterConfig(curatorClient, path);
                    }
                }
            }

            if (eventType == Watcher.Event.EventType.NodeChildrenChanged) {
                if (path.startsWith(Constants.REDIS_ITEM_PATH)) {
                    synchronized (itemChangeLock) {
                        processItems(curatorClient, path);
                    }
                }
            }
        }

    }

    //processClusterConfig
    private void processClusterConfig(CuratorFramework curatorClient, String path) {
        String clusterName = ClusterEnvironment.getClusterName();
        String clusterPath = Constants.getRealClusterPath(clusterName);
        if (clusterPath.equals(path)) {
            boolean exists = ZkClients.exists(curatorClient, path, true);
            if (exists) {
                String data = ZkClients.getData(curatorClient, path, true);
                if (StringUtils.isBlank(data)) {
                    LOGGER.warn("{} cluster has no data", clusterName);
                    return;
                }
                Cluster cluster = JsonUtils.convertObject(data, Cluster.class);
                ClusterChangeListener.handleCluster(cluster);
            }
        }
    }

    //processItems
    private void processItems(CuratorFramework curatorClient, String path) {
        String clusterName = ClusterEnvironment.getClusterName();
        String businessLineName = ClusterEnvironment.getBusinessLineName();
        String itemParentPath = Constants.getRealParentItemPath(clusterName, businessLineName);
        if (path.equals(itemParentPath)) {
            List<String> itemPaths = ZkClients.getChildren(curatorClient, path, true);
            if (itemPaths != null && itemPaths.size() > 0) {
                Map<String, String> map = new LinkedHashMap<>();
                for (String itemPath : itemPaths) {
                    String realItemPath = Constants.getRealItemPath(clusterName, businessLineName, itemPath);
                    String data = ZkClients.getData(curatorClient, realItemPath, false);
                    map.put(itemPath, data);
                }
                ItemChangeListener.handleItems(map);
            }
        }
    }

}
