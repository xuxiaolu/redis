package com.xuxl.redis.admin.mapper;

import com.xuxl.redis.admin.entity.Cluster;
import com.xuxl.redis.admin.entity.ClusterCriteria;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ClusterMapper {
    long countByExample(ClusterCriteria example);

    int deleteByExample(ClusterCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(Cluster record);

    int insertSelective(Cluster record);

    List<Cluster> selectByExample(ClusterCriteria example);

    Cluster selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Cluster record, @Param("example") ClusterCriteria example);

    int updateByExample(@Param("record") Cluster record, @Param("example") ClusterCriteria example);

    int updateByPrimaryKeySelective(Cluster record);

    int updateByPrimaryKey(Cluster record);
}