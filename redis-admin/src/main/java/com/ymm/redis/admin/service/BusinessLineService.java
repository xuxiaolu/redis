package com.ymm.redis.admin.service;

import com.ymm.redis.admin.entity.BusinessLine;

import java.util.List;

public interface BusinessLineService {

    boolean addBusinessLine(String clusterName,String name,String desc);

    List<BusinessLine> getAllBusinessLines();

    BusinessLine getBusinessLineByName(String name);

    List<BusinessLine> getBusinessLinesByClusterName(String clusterName);


}
