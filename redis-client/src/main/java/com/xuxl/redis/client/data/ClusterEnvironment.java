package com.xuxl.redis.client.data;

public class ClusterEnvironment {

    private static String clusterName = null;

    private static String businessLineName = null;

    public static void setClusterName(String clusterName) {
        clusterName = trimToNull(clusterName);
        ClusterEnvironment.clusterName = clusterName;
    }

    public static String getClusterName() {
        return ClusterEnvironment.clusterName;
    }

    public static String getBusinessLineName() {
        return businessLineName;
    }

    public static void setBusinessLineName(String businessLineName) {
        businessLineName = trimToNull(businessLineName);
        ClusterEnvironment.businessLineName = businessLineName;
    }

    private static String trimToNull(String key) {
        if (key == null) {
            return null;
        } else {
            key = key.trim();
            return key.length() == 0 ? null : key;
        }
    }
}
