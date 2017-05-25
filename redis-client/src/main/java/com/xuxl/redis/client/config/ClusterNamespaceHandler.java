package com.xuxl.redis.client.config;


import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * <cluster:annotation-config/>的命名空间
 */
public class ClusterNamespaceHandler extends NamespaceHandlerSupport {

    @Override
    public void init() {
        registerBeanDefinitionParser("annotation-config", new ClusterBeanDefinitionParser());
    }
}
