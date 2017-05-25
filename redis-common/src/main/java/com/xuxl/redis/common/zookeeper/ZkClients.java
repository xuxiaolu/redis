package com.xuxl.redis.common.zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ZkClients {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZkClients.class);

    private ZkClients() {
    }

    /**
     * 创建zkClient
     *
     * @param connectString
     * @param retryPolicy
     * @return
     */
    public static CuratorFramework newClient(String connectString, RetryPolicy retryPolicy) {
        return CuratorFrameworkFactory.newClient(connectString, retryPolicy);
    }

    /**
     * 创建zkClient
     *
     * @param connectString
     * @param sessionTimeoutMs
     * @param connectionTimeoutMs
     * @param retryPolicy
     * @return
     */
    public static CuratorFramework newClient(String connectString, int sessionTimeoutMs, int connectionTimeoutMs, RetryPolicy retryPolicy) {
        return CuratorFrameworkFactory.newClient(connectString, sessionTimeoutMs, connectionTimeoutMs, retryPolicy);
    }

    public static void start(CuratorFramework curatorClient) {
        curatorClient.start();
        try {
            if (!curatorClient.getZookeeperClient().blockUntilConnectedOrTimedOut()) {
                try {
                    curatorClient.getZookeeperClient().getZooKeeper();
                } catch (Exception e) {
                    LOGGER.error("zk start has error", e);
                    curatorClient.close();
                }
            }
        } catch (InterruptedException e) {
            LOGGER.error("zk start error", e);
        }
    }

    /**
     * 检查path是否存在
     *
     * @param curatorClient
     * @param path
     * @param watched
     * @return
     */
    public static boolean exists(CuratorFramework curatorClient, String path, boolean watched) {
        Stat stat;
        try {
            if (watched) {
                stat = curatorClient.checkExists().watched().forPath(path);
            } else {
                stat = curatorClient.checkExists().forPath(path);
            }
            return stat != null;
        } catch (Exception e) {
            String info = String.format("check exists for path %s error", path);
            LOGGER.error(info, e);
            throw new ZkException(info, e);
        }
    }

    /**
     * 创建path(自动创建父path)
     *
     * @param curatorClient
     * @param path
     * @return 创建是否成功
     */
    public static boolean createPath(CuratorFramework curatorClient, String path, boolean ephemeral) {
        try {
            if (ephemeral) {
                curatorClient.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path);
            } else {
                curatorClient.create().creatingParentsIfNeeded().forPath(path);
            }
            return true;
        } catch (Exception e) {
            String info = String.format("create path %s error", path);
            LOGGER.error(info, e);
            throw new ZkException(info, e);
        }
    }

    /**
     * 创建path并添加data(自动创建父path)
     *
     * @param curatorClient
     * @param path
     * @param contents
     * @return
     */
    public static boolean createPath(CuratorFramework curatorClient, String path, byte[] contents, boolean ephemeral) {
        try {
            if (ephemeral) {
                curatorClient.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path, contents);
            } else {
                curatorClient.create().creatingParentsIfNeeded().forPath(path, contents);
            }
            return true;
        } catch (Exception e) {
            String info = String.format("create path %s error", path);
            LOGGER.error(info, e);
            throw new ZkException(info, e);
        }
    }

    /**
     * 获取data
     *
     * @param curatorClient
     * @param path
     * @param watched
     * @return 有数据返回数据，无数据返回null
     */
    public static String getData(CuratorFramework curatorClient, String path, boolean watched) {
        byte[] bytes;
        try {
            if (watched) {
                bytes = curatorClient.getData().watched().forPath(path);
            } else {
                bytes = curatorClient.getData().forPath(path);
            }
            if (bytes != null) {
                return new String(bytes, "UTF-8");
            }
            return null;
        } catch (Exception e) {
            String info = String.format("get data for path %s error", path);
            LOGGER.error(info, e);
            throw new ZkException(info, e);
        }
    }


    /**
     * set数据
     *
     * @param curatorClient
     * @param path
     * @param bytes
     */
    public static void setData(CuratorFramework curatorClient, String path, byte[] bytes) {
        try {
            curatorClient.setData().forPath(path, bytes);
        } catch (Exception e) {
            String info = String.format("set data for path %s error", path);
            LOGGER.error(info, e);
            throw new ZkException(info, e);
        }
    }


    public static List<String> getChildren(CuratorFramework curatorClient, String path, boolean watched) {
        List<String> children;
        try {
            if (watched) {
                children = curatorClient.getChildren().watched().forPath(path);
            } else {
                children = curatorClient.getChildren().forPath(path);
            }
            return children;
        } catch (Exception e) {
            String info = String.format("get children for path %s error", path);
            LOGGER.error(info, e);
            throw new ZkException(info, e);
        }
    }

    public static void delete(CuratorFramework curatorClient, String path) {
        try {
            curatorClient.delete().forPath(path);
        } catch (Exception e) {
            String info = String.format("delete path %s error", path);
            LOGGER.error(info, e);
            throw new ZkException(info, e);
        }
    }

}
