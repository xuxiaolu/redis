package com.ymm.redis.admin.controller;

import com.ymm.redis.admin.entity.BusinessLine;
import com.ymm.redis.admin.exception.BizException;
import com.ymm.redis.admin.response.BizResponse;
import com.ymm.redis.admin.service.BusinessLineService;
import com.ymm.redis.admin.utils.CodeConstants;
import com.ymm.redis.admin.vo.BusinessLineVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/bizline")
public class BusinessLineController {

    @Autowired
    private BusinessLineService businessLineService;


    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public BizResponse addBusinessLine(@RequestParam String clusterName, @RequestParam String name, String desc) {
        clusterName = StringUtils.trimToNull(clusterName);
        if (StringUtils.isBlank(clusterName)) {
            throw new BizException(CodeConstants.PARAM_ERROR_CODE, "集群名字不能为空");
        }

        name = StringUtils.trimToNull(name);
        if (StringUtils.isBlank(name)) {
            throw new BizException(CodeConstants.PARAM_ERROR_CODE, "业务线名字不能为空");
        }

        boolean result = businessLineService.addBusinessLine(clusterName, name, desc);

        BizResponse response = new BizResponse();
        if (!result) {
            response.setCode(CodeConstants.ADD_BUSINESS_LINE_ERROR_CODE);
            response.setMsg(CodeConstants.ADD_BUSINESS_LINE_ERROR_MSG);
        }
        return response;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public BizResponse getBusinessLines(@RequestParam String clusterName) {
        clusterName = StringUtils.trimToNull(clusterName);
        if (StringUtils.isBlank(clusterName)) {
            throw new BizException(CodeConstants.PARAM_ERROR_CODE, "集群名字不能为空");
        }

        List<BusinessLine> businessLines = businessLineService.getBusinessLinesByClusterName(clusterName);

        BizResponse response = new BizResponse();
        if (businessLines != null && businessLines.size() > 0) {
            List<BusinessLineVo> businessLineVos = new ArrayList<>();
            BusinessLineVo businessLineVo;
            for (BusinessLine businessLine : businessLines) {
                businessLineVo = new BusinessLineVo();
                BeanUtils.copyProperties(businessLine, businessLineVo);
                businessLineVos.add(businessLineVo);
            }
            response.setData(businessLineVos);
        }
        return response;
    }


}
