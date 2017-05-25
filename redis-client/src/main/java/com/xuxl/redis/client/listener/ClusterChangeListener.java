package com.xuxl.redis.client.listener;

import com.xuxl.redis.client.data.ConfigManager;
import com.xuxl.redis.common.dto.Cluster;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.ClusterCommandExecutor;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.util.ReflectionUtils;
import redis.clients.jedis.JedisCluster;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;


public class ClusterChangeListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClusterChangeListener.class);

    private ClusterChangeListener() {
    }

    public static void handleCluster(Cluster cluster) {
        boolean isChanged = ConfigManager.getInstance().setCluster(cluster);
        if (isChanged) {
            try {
                Set<RedisNode> redisNodes = ConfigManager.getInstance().getRedisNodes();
                RedisClusterConfiguration redisClusterConfiguration = SpringContextUtils.getBean(RedisClusterConfiguration.class);
                redisClusterConfiguration.setClusterNodes(redisNodes);
                JedisConnectionFactory jedisConnectionFactory = SpringContextUtils.getBean(JedisConnectionFactory.class);
                Class<?> jedisConnectionFactoryClass = jedisConnectionFactory.getClass();

                //oldCluster
                Field clusterField = ReflectionUtils.findField(jedisConnectionFactoryClass, "cluster", JedisCluster.class);
                ReflectionUtils.makeAccessible(clusterField);
                JedisCluster oldCluster = (JedisCluster) ReflectionUtils.getField(clusterField, jedisConnectionFactory);

                //oldClusterCommandExecutor
                Field clusterCommandExecutorField = ReflectionUtils.findField(jedisConnectionFactoryClass, "clusterCommandExecutor", ClusterCommandExecutor.class);
                ReflectionUtils.makeAccessible(clusterCommandExecutorField);
                ClusterCommandExecutor oldClusterCommandExecutor = (ClusterCommandExecutor) ReflectionUtils.getField(clusterCommandExecutorField, jedisConnectionFactory);

                String password = ConfigManager.getInstance().getPassword();
                if (StringUtils.isNotBlank(password)) {
                    jedisConnectionFactory.setPassword(password);
                }

                //set redisClusterConfiguration
                Field clusterConfigField = ReflectionUtils.findField(jedisConnectionFactoryClass, "clusterConfig", RedisClusterConfiguration.class);
                ReflectionUtils.makeAccessible(clusterConfigField);
                ReflectionUtils.setField(clusterConfigField, jedisConnectionFactory, redisClusterConfiguration);
                clusterConfigField.setAccessible(false);

                //set new cluster and clusterCommandExecutor
                Method createClusterMethod = ReflectionUtils.findMethod(jedisConnectionFactoryClass, "createCluster");
                ReflectionUtils.makeAccessible(createClusterMethod);
                Object newCluster = ReflectionUtils.invokeMethod(createClusterMethod, jedisConnectionFactory);
                createClusterMethod.setAccessible(false);
                ReflectionUtils.setField(clusterField, jedisConnectionFactory, newCluster);

                clusterField.setAccessible(false);
                clusterCommandExecutorField.setAccessible(false);

                //close oldCluster
                if (oldCluster != null) {
                    try {
                        oldCluster.close();
                    } catch (Exception ex) {
                        LOGGER.warn("Cannot properly close Jedis cluster", ex);
                    }
                }

                //close oldClusterCommandExecutor
                if (oldClusterCommandExecutor != null) {
                    try {
                        oldClusterCommandExecutor.destroy();
                    } catch (Exception ex) {
                        LOGGER.warn("Cannot properly close cluster command executor", ex);
                    }
                }
            } catch (Exception e) {
                LOGGER.error("change has error", e);
            }
        }

    }

}
