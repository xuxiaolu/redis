package com.xuxl.redis.admin.controller;


import com.xuxl.redis.admin.response.BizResponse;
import com.xuxl.redis.admin.service.BusinessLineService;
import com.xuxl.redis.admin.service.ClusterService;
import com.xuxl.redis.admin.vo.RedisClusterDetailVo;
import com.xuxl.redis.admin.vo.RedisClusterVo;
import com.xuxl.redis.admin.entity.BusinessLine;
import com.xuxl.redis.admin.entity.Cluster;
import com.xuxl.redis.admin.exception.BizException;
import com.xuxl.redis.admin.utils.CodeConstants;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/cluster")
@RestController
public class ClusterController {

    @Autowired
    private ClusterService clusterService;

    @Autowired
    private BusinessLineService businessLineService;

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public BizResponse delCluster(@RequestParam String name) {

        name = StringUtils.trimToNull(name);
        if (StringUtils.isBlank(name)) {
            throw new BizException(CodeConstants.PARAM_ERROR_CODE, "集群名字不能为空");
        }

        boolean result = clusterService.deleteCluster(name);

        BizResponse response = new BizResponse();
        if (!result) {
            response.setCode(CodeConstants.DEL_CLUSTER_ERROR_CODE);
            response.setMsg(CodeConstants.DEL_CLUSTER_ERROR_MSG);
        }
        return response;
    }


    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public BizResponse getList() {
        BizResponse response = new BizResponse();
        List<Cluster> clusters = clusterService.getAllAvailableClusters();
        if (clusters != null && clusters.size() > 0) {
            List<RedisClusterVo> redisClusterVos = new ArrayList<>(clusters.size());
            for (Cluster cluster : clusters) {
                RedisClusterVo redisClusterVo = new RedisClusterVo();
                BeanUtils.copyProperties(cluster, redisClusterVo);
                redisClusterVos.add(redisClusterVo);
            }
            response.setData(redisClusterVos);
        }
        return response;
    }


    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public BizResponse getDetail(@RequestParam String name) {

        name = StringUtils.trimToNull(name);
        if (StringUtils.isBlank(name)) {
            throw new BizException(CodeConstants.PARAM_ERROR_CODE, "集群名字不能为空");
        }

        Cluster cluster = clusterService.getAvailableClusterByName(name);
        if (cluster == null) {
            throw new BizException(CodeConstants.CLUSTER_NOT_EXISTS_ERROR_CODE, String.format("%s不存在", name));
        }

        RedisClusterDetailVo clusterDetailVo = new RedisClusterDetailVo();

        RedisClusterVo redisClusterVo = new RedisClusterVo();
        BeanUtils.copyProperties(cluster, redisClusterVo);
        clusterDetailVo.setCluster(redisClusterVo);

        List<BusinessLine> businessLines = businessLineService.getBusinessLinesByClusterName(name);
        if (businessLines != null && !businessLines.isEmpty()) {
            List<String> businessLineNames = new ArrayList<>();
            for (BusinessLine businessLine : businessLines) {
                businessLineNames.add(businessLine.getName());
            }
            clusterDetailVo.setBusinessLines(businessLineNames);
        }

        BizResponse response = new BizResponse();
        response.setData(clusterDetailVo);
        return response;
    }


    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public BizResponse addCluster(
            @RequestParam String name,
            @RequestParam String hostAndPorts,
            @RequestParam(required = false) String password,
            @RequestParam(required = false) String desc) {
        name = StringUtils.trimToNull(name);
        if (StringUtils.isBlank(name)) {
            throw new BizException(CodeConstants.PARAM_ERROR_CODE, "集群名字不能为空");
        }

        hostAndPorts = StringUtils.trimToNull(hostAndPorts);
        if (StringUtils.isBlank(hostAndPorts)) {
            throw new BizException(CodeConstants.PARAM_ERROR_CODE, "主机和端口号不能为空");
        }
        password = StringUtils.trimToNull(password);
        desc = StringUtils.trimToNull(desc);

        boolean result = clusterService.addCluster(name, hostAndPorts, password, desc);

        BizResponse response = new BizResponse();
        if (!result) {
            response.setCode(CodeConstants.ADD_CLUSTER_ERROR_CODE);
            response.setMsg(CodeConstants.ADD_CLUSTER_ERROR_MSG);
        }
        return response;
    }


    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public BizResponse updateCluster(
            @RequestParam String name,
            @RequestParam String hostAndPorts,
            @RequestParam(required = false) String password,
            @RequestParam(required = false) String desc) {
        name = StringUtils.trimToNull(name);
        if (StringUtils.isBlank(name)) {
            throw new BizException(CodeConstants.PARAM_ERROR_CODE, "集群名字不能为空");
        }

        hostAndPorts = StringUtils.trimToNull(hostAndPorts);
        if (StringUtils.isBlank(hostAndPorts)) {
            throw new BizException(CodeConstants.PARAM_ERROR_CODE, "主机和端口号不能为空");
        }
        password = StringUtils.trimToNull(password);
        desc = StringUtils.trimToNull(desc);

        boolean result = clusterService.updateCluster(name, hostAndPorts, password, desc);

        BizResponse response = new BizResponse();
        if (!result) {
            response.setCode(CodeConstants.UPDATE_CLUSTER_ERROR_CODE);
            response.setMsg(CodeConstants.UPDATE_CLUSTER_ERROR_MSG);
        }
        return response;
    }

}
