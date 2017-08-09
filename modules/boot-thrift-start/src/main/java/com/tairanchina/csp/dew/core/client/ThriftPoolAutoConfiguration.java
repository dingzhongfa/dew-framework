package com.tairanchina.csp.dew.core.client;

import com.tairanchina.csp.dew.core.client.pool.ThriftKey;
import com.tairanchina.csp.dew.core.client.pool.ThriftPooledObjectFactory;
import org.apache.commons.pool2.KeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;
import org.apache.thrift.TServiceClient;
import org.apache.thrift.protocol.TProtocolFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.PropertyResolver;

/**
 * Created by 迹_Jason on 2017/08/09.
 */
@Configuration
public class ThriftPoolAutoConfiguration {

    @Autowired
    private TProtocolFactory protocolFactory;
    @Autowired
    private LoadBalancerClient loadBalancerClient;
    @Autowired
    private PropertyResolver propertyResolver;
    @Value("${thrift.client.max.poolobject:10}")
    private int maxThreads;

    @Bean
    public KeyedObjectPool<ThriftKey, TServiceClient> thriftClientsPool() {
        GenericKeyedObjectPoolConfig poolConfig = new GenericKeyedObjectPoolConfig();
        poolConfig.setMaxTotal(maxThreads);
        poolConfig.setMaxIdlePerKey(maxThreads);
        poolConfig.setMaxTotalPerKey(maxThreads);
        poolConfig.setJmxEnabled(false);
        ThriftPooledObjectFactory thriftPooledObjectFactory = new ThriftPooledObjectFactory();
        thriftPooledObjectFactory.setLoadBalancerClient(loadBalancerClient);
        thriftPooledObjectFactory.setPropertyResolver(propertyResolver);
        thriftPooledObjectFactory.setProtocolFactory(protocolFactory);
        return new GenericKeyedObjectPool(thriftPooledObjectFactory, poolConfig);
    }
}