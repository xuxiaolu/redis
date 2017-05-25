package com.xuxl.redis.client.config;


import com.xuxl.redis.client.zookeeper.ZkClient;
import com.xuxl.redis.client.zookeeper.ZkClientLifecycle;
import com.xuxl.redis.client.SpringRedisClientImpl;
import com.xuxl.redis.client.data.ConfigManager;
import com.xuxl.redis.client.data.ClusterEnvironment;
import com.xuxl.redis.client.listener.SpringContextUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Set;

/**
 * 集群客户端需要配置Bean的工具类
 */
class ClusterBeanRegisterUtils {

    private static final String REDIS_CLUSTER_CONFIGURATION_BEAN_NAME = "redisClusterConfiguration";

    private static final String JEDIS_CONNECTION_FACTORY_BEAN_NAME = "jedisConnectionFactory";

    private static final String REDIS_TEMPLATE_BEAN_NAME = "redisTemplate";

    private static final String REDIS_CLIENT_BEAN_NAME = "redisClient";

    private static final String SPRING_CONTEXT_UTILS_BEAN_NAME = "springContextUtils";

    private static final String ZK_CLIENT_LIFE_CYCLE_BEAN_NAME = "zkClientLifecycle";

    private static ZkClient zkClient = ZkClient.getInstance();

    private ClusterBeanRegisterUtils() {
    }


    static RuntimeBeanReference getJedisPoolConfig(String jedisPoolConfigBeanName) {
        return new RuntimeBeanReference(jedisPoolConfigBeanName);
    }

    static RuntimeBeanReference registerRedisClusterConfiguration(String clusterName, String businessLineName, int maxRedirects, BeanDefinitionRegistry registry) {
        ClusterEnvironment.setClusterName(clusterName);
        ClusterEnvironment.setBusinessLineName(businessLineName);
        Set<RedisNode> redisNodes = zkClient.getRedisNodes();
        RootBeanDefinition redisClusterConfiguration = new RootBeanDefinition(RedisClusterConfiguration.class);
        redisClusterConfiguration.getPropertyValues().add("clusterNodes", redisNodes);
        redisClusterConfiguration.getPropertyValues().add("maxRedirects", maxRedirects);
        registry.registerBeanDefinition(REDIS_CLUSTER_CONFIGURATION_BEAN_NAME, redisClusterConfiguration);
        return new RuntimeBeanReference(REDIS_CLUSTER_CONFIGURATION_BEAN_NAME);
    }

    static RuntimeBeanReference redisJedisConnectionFactory(int timeout, RuntimeBeanReference redisClusterConfiguration, RuntimeBeanReference jedisPoolConfig, BeanDefinitionRegistry registry) {
        RootBeanDefinition jedisConnectionFactory = new RootBeanDefinition(JedisConnectionFactory.class);
        jedisConnectionFactory.getConstructorArgumentValues().addGenericArgumentValue(redisClusterConfiguration);
        jedisConnectionFactory.getPropertyValues().add("poolConfig", jedisPoolConfig);
        jedisConnectionFactory.getPropertyValues().add("timeout", timeout);
        String password = ConfigManager.getInstance().getPassword();
        if (StringUtils.isNotBlank(password)) {
            jedisConnectionFactory.getPropertyValues().add("password", password);
        }
        registry.registerBeanDefinition(JEDIS_CONNECTION_FACTORY_BEAN_NAME, jedisConnectionFactory);
        return new RuntimeBeanReference(JEDIS_CONNECTION_FACTORY_BEAN_NAME);
    }

    static RuntimeBeanReference registerRedisTemplate(RuntimeBeanReference jedisConnectionFactory, BeanDefinitionRegistry registry) {
        RootBeanDefinition redisTemplate = new RootBeanDefinition(StringRedisTemplate.class);
        redisTemplate.getPropertyValues().add("connectionFactory", jedisConnectionFactory);
        registry.registerBeanDefinition(REDIS_TEMPLATE_BEAN_NAME, redisTemplate);
        return new RuntimeBeanReference(REDIS_TEMPLATE_BEAN_NAME);
    }

    static void registerRedisClient(RuntimeBeanReference redisTemplate, BeanDefinitionRegistry registry) {
        RootBeanDefinition redisClient = new RootBeanDefinition(SpringRedisClientImpl.class);
        redisClient.getPropertyValues().add("redisTemplate", redisTemplate);
        registry.registerBeanDefinition(REDIS_CLIENT_BEAN_NAME, redisClient);
    }

    static void registerCuratorClientLifeCycle(BeanDefinitionRegistry registry) {
        RootBeanDefinition zkClientLifecycle = new RootBeanDefinition(ZkClientLifecycle.class);
        zkClientLifecycle.setDestroyMethodName("close");
        registry.registerBeanDefinition(ZK_CLIENT_LIFE_CYCLE_BEAN_NAME, zkClientLifecycle);
    }

    static void registerSpringContextUtils(BeanDefinitionRegistry registry) {
        RootBeanDefinition springContextUtils = new RootBeanDefinition(SpringContextUtils.class);
        registry.registerBeanDefinition(SPRING_CONTEXT_UTILS_BEAN_NAME, springContextUtils);
    }
}
