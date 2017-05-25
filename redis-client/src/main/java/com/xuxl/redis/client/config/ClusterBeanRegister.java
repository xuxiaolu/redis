package com.xuxl.redis.client.config;


import com.xuxl.redis.client.config.annotation.EnableClusterConfig;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 注解配置集群
 * @see EnableClusterConfig
 */
public class ClusterBeanRegister implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {

        AnnotationAttributes attributes = AnnotationAttributes.fromMap(annotationMetadata.getAnnotationAttributes(EnableClusterConfig.class.getName()));

        int timeout = attributes.getNumber("timeout");

        int maxRedirects = attributes.getNumber("maxRedirects");

        String clusterName = attributes.getString("clusterName");

        String businessLineName = attributes.getString("businessLineName");

        String jedisPoolConfigBeanName = attributes.getString("jedisPoolConfig");

        //1.获取JedisPoolConfig
        RuntimeBeanReference jedisPoolConfig = ClusterBeanRegisterUtils.getJedisPoolConfig(jedisPoolConfigBeanName);

        //2.配置RedisClusterConfiguration
        RuntimeBeanReference redisClusterConfiguration = ClusterBeanRegisterUtils.registerRedisClusterConfiguration(clusterName, businessLineName, maxRedirects, beanDefinitionRegistry);

        //3.配置JedisConnectionFactory
        RuntimeBeanReference jedisConnectionFactory = ClusterBeanRegisterUtils.redisJedisConnectionFactory(timeout, redisClusterConfiguration, jedisPoolConfig, beanDefinitionRegistry);

        //4.配置RedisTemplate
        RuntimeBeanReference redisTemplate = ClusterBeanRegisterUtils.registerRedisTemplate(jedisConnectionFactory, beanDefinitionRegistry);

        //5.配置RedisClient
        ClusterBeanRegisterUtils.registerRedisClient(redisTemplate, beanDefinitionRegistry);

        //6.配置CuratorClientLifeCycle
        ClusterBeanRegisterUtils.registerCuratorClientLifeCycle(beanDefinitionRegistry);

        //7.配置SpringContextUtils
        ClusterBeanRegisterUtils.registerSpringContextUtils(beanDefinitionRegistry);
    }
}
