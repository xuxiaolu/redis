package com.xuxl.redis.admin.mapper;

import com.xuxl.redis.admin.entity.BusinessLineCriteria;
import com.xuxl.redis.admin.entity.BusinessLine;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BusinessLineMapper {
    long countByExample(BusinessLineCriteria example);

    int deleteByExample(BusinessLineCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(BusinessLine record);

    int insertSelective(BusinessLine record);

    List<BusinessLine> selectByExample(BusinessLineCriteria example);

    BusinessLine selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") BusinessLine record, @Param("example") BusinessLineCriteria example);

    int updateByExample(@Param("record") BusinessLine record, @Param("example") BusinessLineCriteria example);

    int updateByPrimaryKeySelective(BusinessLine record);

    int updateByPrimaryKey(BusinessLine record);
}