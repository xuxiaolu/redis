package com.ymm.redis.admin.service.impl;

import com.ymm.redis.admin.entity.BusinessLine;
import com.ymm.redis.admin.entity.BusinessLineCriteria;
import com.ymm.redis.admin.exception.BizException;
import com.ymm.redis.admin.mapper.BusinessLineMapper;
import com.ymm.redis.admin.service.BusinessLineService;
import com.ymm.redis.admin.utils.CodeConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BusinessLineServiceImpl implements BusinessLineService {

    @Autowired
    private BusinessLineMapper businessLineMapper;

    @Override
    public boolean addBusinessLine(String clusterName, String name, String desc) {
        BusinessLine businessLineByName = this.getBusinessLineByName(name);
        if (businessLineByName != null) {
            throw new BizException(CodeConstants.BUSINESS_LINE_EXISTS_ERROR_CODE, String.format("%s业务线已存在", name));
        }

        Date now = new Date();

        BusinessLine businessLine = new BusinessLine();
        businessLine.setClusterName(clusterName);
        businessLine.setName(name);
        businessLine.setDescription(desc);
        businessLine.setCreateTime(now);
        businessLine.setModifyTime(now);

        return 1 == businessLineMapper.insertSelective(businessLine);
    }

    @Override
    public List<BusinessLine> getAllBusinessLines() {
        return businessLineMapper.selectByExample(new BusinessLineCriteria());
    }

    @Override
    public BusinessLine getBusinessLineByName(String name) {
        BusinessLineCriteria businessLineCriteria = new BusinessLineCriteria();
        businessLineCriteria.createCriteria().andNameEqualTo(name);
        List<BusinessLine> businessLines = businessLineMapper.selectByExample(businessLineCriteria);
        return businessLines == null || businessLines.isEmpty() ? null : businessLines.get(0);
    }

    @Override
    public List<BusinessLine> getBusinessLinesByClusterName(String clusterName) {
        BusinessLineCriteria businessLineCriteria = new BusinessLineCriteria();
        businessLineCriteria.createCriteria().andClusterNameEqualTo(clusterName);
        return businessLineMapper.selectByExample(businessLineCriteria);
    }
}
