package com.ymm.redis.admin.mapper;

import com.ymm.redis.admin.entity.Item;
import com.ymm.redis.admin.entity.ItemCriteria;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ItemMapper {
    long countByExample(ItemCriteria example);

    int deleteByExample(ItemCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(Item record);

    int insertSelective(Item record);

    List<Item> selectByExample(ItemCriteria example);

    Item selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Item record, @Param("example") ItemCriteria example);

    int updateByExample(@Param("record") Item record, @Param("example") ItemCriteria example);

    int updateByPrimaryKeySelective(Item record);

    int updateByPrimaryKey(Item record);
}