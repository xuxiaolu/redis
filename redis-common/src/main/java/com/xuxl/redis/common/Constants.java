package com.xuxl.redis.common;


import org.springframework.util.Assert;

public class Constants {

    public static final String KEY_ZK_ADDRESS = "redis.cluster.zk.address";

    public static final String REDIS_CLUSTER_PATH = "/cache/redis/cluster";

    public static final String REDIS_ITEM_PATH = "/cache/redis/item";

    private static final String PATH_SEPARATOR = "/";

    public static String getRealClusterPath(String clusterName) {
        Assert.hasText(clusterName, "clusterName can not be empty");
        StringBuilder builder = new StringBuilder(REDIS_CLUSTER_PATH);
        builder.append(PATH_SEPARATOR).append(clusterName);
        return builder.toString();
    }

    public static String getRealParentItemPath(String clusterName, String businessLineName) {
        Assert.hasText(clusterName, "clusterName can not be empty");
        Assert.hasText(businessLineName, "businessLineName can not be empty");
        StringBuilder builder = new StringBuilder(REDIS_ITEM_PATH);
        builder.append(PATH_SEPARATOR).append(clusterName);
        builder.append(PATH_SEPARATOR).append(businessLineName);
        return builder.toString();
    }

    public static String getRealItemPath(String clusterName, String businessLineName, String itemName) {
        Assert.hasText(clusterName, "clusterName can not be empty");
        Assert.hasText(businessLineName, "businessLineName can not be empty");
        Assert.hasText(itemName, "itemName can not be empty");
        StringBuilder builder = new StringBuilder(REDIS_ITEM_PATH);
        builder.append(PATH_SEPARATOR).append(clusterName);
        builder.append(PATH_SEPARATOR).append(businessLineName);
        builder.append(PATH_SEPARATOR).append(itemName);
        return builder.toString();
    }

}
