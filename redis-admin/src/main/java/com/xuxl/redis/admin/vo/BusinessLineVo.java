package com.xuxl.redis.admin.vo;

import java.io.Serializable;
import java.util.Date;


public class BusinessLineVo implements Serializable {

    private static final long serialVersionUID = -2629395710865620837L;

    private String clusterName;

    private String name;

    private String description;

    private Date createTime;

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
