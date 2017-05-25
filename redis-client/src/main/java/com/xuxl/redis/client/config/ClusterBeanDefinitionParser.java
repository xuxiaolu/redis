package com.xuxl.redis.client.config;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import redis.clients.jedis.Protocol;

/**
 * XML配置集群的解析类
 */
public class ClusterBeanDefinitionParser implements BeanDefinitionParser {

    public BeanDefinition parse(Element element, ParserContext parserContext) {

        BeanDefinitionRegistry registry = parserContext.getRegistry();

        //1.获取JedisPoolConfig
        String jedisPoolConfigBeanName = element.getAttribute("jedis-pool-config");
        RuntimeBeanReference jedisPoolConfig = ClusterBeanRegisterUtils.getJedisPoolConfig(jedisPoolConfigBeanName);

        //2.配置RedisClusterConfiguration
        String clusterName = element.getAttribute("cluster-name");
        String businessLineName = element.getAttribute("business-line-name");
        String maxRedirectsString = element.getAttribute("max-redirects");
        int maxRedirects = StringUtils.isBlank(maxRedirectsString) ? 5 : Integer.valueOf(maxRedirectsString);
        RuntimeBeanReference redisClusterConfiguration = ClusterBeanRegisterUtils.registerRedisClusterConfiguration(clusterName, businessLineName, maxRedirects, registry);

        //3.配置JedisConnectionFactory
        String timeoutStr = element.getAttribute("timeout");
        int timeout = StringUtils.isBlank(timeoutStr) ? Protocol.DEFAULT_TIMEOUT : Integer.valueOf(timeoutStr);
        RuntimeBeanReference jedisConnectionFactory = ClusterBeanRegisterUtils.redisJedisConnectionFactory(timeout, redisClusterConfiguration, jedisPoolConfig, registry);

        //4.配置RedisTemplate
        RuntimeBeanReference redisTemplate = ClusterBeanRegisterUtils.registerRedisTemplate(jedisConnectionFactory, registry);

        //5.配置RedisClient
        ClusterBeanRegisterUtils.registerRedisClient(redisTemplate, registry);

        //6.配置CuratorClientLifeCycle
        ClusterBeanRegisterUtils.registerCuratorClientLifeCycle(registry);

        //7.配置SpringContextUtils
        ClusterBeanRegisterUtils.registerSpringContextUtils(registry);

        return null;
    }

}
