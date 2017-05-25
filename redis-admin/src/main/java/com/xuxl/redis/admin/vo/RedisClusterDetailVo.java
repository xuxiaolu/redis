package com.xuxl.redis.admin.vo;

import java.io.Serializable;
import java.util.List;

public class RedisClusterDetailVo implements Serializable {

    private static final long serialVersionUID = -3717871346194404048L;

    private RedisClusterVo cluster;

    private List<String> businessLines;

    public RedisClusterVo getCluster() {
        return cluster;
    }

    public void setCluster(RedisClusterVo cluster) {
        this.cluster = cluster;
    }

    public List<String> getBusinessLines() {
        return businessLines;
    }

    public void setBusinessLines(List<String> businessLines) {
        this.businessLines = businessLines;
    }
}
