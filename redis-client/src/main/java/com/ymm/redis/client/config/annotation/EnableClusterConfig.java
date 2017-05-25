package com.ymm.redis.client.config.annotation;


import com.ymm.redis.client.config.ClusterBeanRegister;
import org.springframework.context.annotation.Import;
import redis.clients.jedis.Protocol;

import java.lang.annotation.*;


/**
 * 自动配置集群的注解
 *
 * @see ClusterBeanRegister
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(ClusterBeanRegister.class)
public @interface EnableClusterConfig {

    /**
     * jedisPoolConfig配置的BeanName
     */
    String jedisPoolConfig();

    /**
     * 连接redis的timeout，默认为2s
     */
    int timeout() default Protocol.DEFAULT_TIMEOUT;

    /**
     * 最大重试次数，默认为5次
     */
    int maxRedirects() default 5;

    /**
     * 集群名字
     */
    String clusterName();

    /**
     * 业务线名字
     */
    String businessLineName();
}
