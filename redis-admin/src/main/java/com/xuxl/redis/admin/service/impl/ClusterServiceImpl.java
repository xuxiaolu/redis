package com.xuxl.redis.admin.service.impl;

import com.xuxl.redis.admin.entity.ClusterCriteria;
import com.xuxl.redis.admin.exception.BizException;
import com.xuxl.redis.admin.mapper.ClusterMapper;
import com.xuxl.redis.admin.service.ClusterService;
import com.xuxl.redis.admin.utils.CodeConstants;
import com.xuxl.redis.admin.zookeeper.ZkClient;
import com.xuxl.redis.common.JsonUtils;
import com.xuxl.redis.common.dto.Cluster;
import com.xuxl.redis.common.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ClusterServiceImpl implements ClusterService {

    @Autowired
    private ClusterMapper clusterMapper;

    @Override
    public boolean addCluster(String name, String hostAndPorts, String password, String desc) {
        com.xuxl.redis.admin.entity.Cluster clusterByName = this.getClusterNoMatterIsDeleteByName(name);
        if (clusterByName != null) {
            throw new BizException(CodeConstants.CLUSTER_EXISTS_ERROR_CODE, String.format("%s已存在", name));
        }

        //1.集群信息保存到DB
        Date now = new Date();

        final com.xuxl.redis.admin.entity.Cluster cluster = new com.xuxl.redis.admin.entity.Cluster();
        cluster.setName(name);
        cluster.setHostAndPorts(hostAndPorts);
        cluster.setPassword(password);
        cluster.setDescription(desc);
        cluster.setCreateTime(now);
        cluster.setModifyTime(now);
        cluster.setIsDelete(0);

        Boolean result = 1 == clusterMapper.insert(cluster);

        if (result) {
            //2.集群信息发布到ZK
            String clusterPath = Constants.getRealClusterPath(name);
            Cluster clusterConfig = new Cluster();
            clusterConfig.setHostAndPorts(hostAndPorts);
            clusterConfig.setPassword(password);
            String data = JsonUtils.convertJson(clusterConfig);
            ZkClient.getInstance().create(clusterPath, data, false);
        }

        return result;
    }

    @Override
    public boolean deleteCluster(String name) {
        com.xuxl.redis.admin.entity.Cluster cluster = this.getAvailableClusterByName(name);
        if (cluster == null) {
            throw new BizException(CodeConstants.CLUSTER_NOT_EXISTS_ERROR_CODE, String.format("%s不存在", name));
        }

        cluster.setIsDelete(1);
        cluster.setModifyTime(new Date());

        //1.软删除集群
        boolean result = 1 == clusterMapper.updateByPrimaryKey(cluster);

        if (result) {
            //2.从ZK中删除集群
            String clusterPath = Constants.getRealClusterPath(name);
            ZkClient.getInstance().delete(clusterPath);
        }

        return result;
    }

    @Override
    public boolean updateCluster(String name, String hostAndPorts, String password, String desc) {
        com.xuxl.redis.admin.entity.Cluster cluster = this.getAvailableClusterByName(name);
        if (cluster == null) {
            throw new BizException(CodeConstants.CLUSTER_NOT_EXISTS_ERROR_CODE, String.format("%s不存在", name));
        }

        cluster.setHostAndPorts(hostAndPorts);
        cluster.setPassword(password);
        cluster.setDescription(desc);
        cluster.setModifyTime(new Date());

        //1.集群信息更新到DB
        boolean result = 1 == clusterMapper.updateByPrimaryKey(cluster);

        if (result) {
            //2.集群信息更新到ZK
            String clusterPath = Constants.getRealClusterPath(cluster.getName());
            Cluster clusterConfig = new Cluster();
            clusterConfig.setHostAndPorts(cluster.getHostAndPorts());
            clusterConfig.setPassword(cluster.getPassword());
            String data = JsonUtils.convertJson(clusterConfig);
            ZkClient.getInstance().setData(clusterPath, data);
        }
        return result;
    }

    @Override
    public com.xuxl.redis.admin.entity.Cluster getAvailableClusterByName(String name) {
        ClusterCriteria clusterCriteria = new ClusterCriteria();
        clusterCriteria.createCriteria().andNameEqualTo(name).andIsDeleteEqualTo(0);
        List<com.xuxl.redis.admin.entity.Cluster> clusters = clusterMapper.selectByExample(clusterCriteria);
        return clusters == null || clusters.isEmpty() ? null : clusters.get(0);
    }

    @Override
    public com.xuxl.redis.admin.entity.Cluster getUnAvailableClusterByName(String name) {
        ClusterCriteria clusterCriteria = new ClusterCriteria();
        clusterCriteria.createCriteria().andNameEqualTo(name).andIsDeleteEqualTo(1);
        List<com.xuxl.redis.admin.entity.Cluster> clusters = clusterMapper.selectByExample(clusterCriteria);
        return clusters == null || clusters.isEmpty() ? null : clusters.get(0);
    }

    @Override
    public com.xuxl.redis.admin.entity.Cluster getClusterNoMatterIsDeleteByName(String name) {
        ClusterCriteria clusterCriteria = new ClusterCriteria();
        clusterCriteria.createCriteria().andNameEqualTo(name);
        List<com.xuxl.redis.admin.entity.Cluster> clusters = clusterMapper.selectByExample(clusterCriteria);
        return clusters == null || clusters.isEmpty() ? null : clusters.get(0);
    }

    @Override
    public List<com.xuxl.redis.admin.entity.Cluster> getAllAvailableClusters() {
        ClusterCriteria clusterCriteria = new ClusterCriteria();
        clusterCriteria.createCriteria().andIsDeleteEqualTo(0);
        return clusterMapper.selectByExample(clusterCriteria);
    }

    @Override
    public List<com.xuxl.redis.admin.entity.Cluster> getAllUnAvailableClusters() {
        ClusterCriteria clusterCriteria = new ClusterCriteria();
        clusterCriteria.createCriteria().andIsDeleteEqualTo(1);
        return clusterMapper.selectByExample(clusterCriteria);
    }

}
