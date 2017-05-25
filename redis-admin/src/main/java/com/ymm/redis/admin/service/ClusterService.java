package com.ymm.redis.admin.service;

import com.ymm.redis.admin.entity.Cluster;

import java.util.List;

public interface ClusterService {


    /**
     * 添加集群
     *
     * @param name
     * @param hostAndPorts
     * @param password
     * @param desc
     * @return
     */
    boolean addCluster(String name, String hostAndPorts, String password, String desc);

    /**
     * 删除集群（软删除）
     *
     * @param name
     * @return
     */
    boolean deleteCluster(String name);


    /**
     * 更新集群
     *
     * @param name
     * @param hostAndPorts
     * @param password
     * @param desc
     * @return
     */
    boolean updateCluster(String name, String hostAndPorts, String password, String desc);

    /**
     * 获取可用的集群
     *
     * @param name
     * @return
     */
    Cluster getAvailableClusterByName(String name);


    /**
     * 获取不可用的集群
     *
     * @param name
     * @return
     */
    Cluster getUnAvailableClusterByName(String name);

    /**
     * 获取集群（不在乎是否删除）
     *
     * @param name
     * @return
     */
    Cluster getClusterNoMatterIsDeleteByName(String name);

    /**
     * 获取所有可用集群
     * @return
     */
    List<Cluster> getAllAvailableClusters();


    List<Cluster> getAllUnAvailableClusters();

}
