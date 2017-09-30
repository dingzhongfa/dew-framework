package com.tairanchina.csp.dew.rpc.thrift.client;

import com.tairanchina.csp.dew.rpc.thrift.client.pool.ThriftKey;
import com.tairanchina.csp.dew.rpc.thrift.client.pool.ThriftPooledObjectFactory;
import org.apache.commons.pool2.KeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;
import org.apache.thrift.TServiceClient;
import org.apache.thrift.protocol.TProtocolFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
    private ThriftProperty thriftProperty;

    @Bean
    public KeyedObjectPool<ThriftKey, TServiceClient> thriftClientsPool() {
        GenericKeyedObjectPoolConfig poolConfig = new GenericKeyedObjectPoolConfig();
        poolConfig.setMaxTotal(thriftProperty.getClient().getMaxThread());
        poolConfig.setMaxIdlePerKey(thriftProperty.getClient().getMaxThread());
        poolConfig.setMaxTotalPerKey(thriftProperty.getClient().getMaxThread());
        poolConfig.setJmxEnabled(false);
        ThriftPooledObjectFactory thriftPooledObjectFactory = new ThriftPooledObjectFactory();
        thriftPooledObjectFactory.setLoadBalancerClient(loadBalancerClient);
        thriftPooledObjectFactory.setThriftProperty(thriftProperty);
        thriftPooledObjectFactory.setProtocolFactory(protocolFactory);
        return new GenericKeyedObjectPool(thriftPooledObjectFactory, poolConfig);
    }
}