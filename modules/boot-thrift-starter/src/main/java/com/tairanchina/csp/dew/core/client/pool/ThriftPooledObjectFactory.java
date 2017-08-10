package com.tairanchina.csp.dew.core.client.pool;

import com.tairanchina.csp.dew.core.client.ThriftProperty;
import com.tairanchina.csp.dew.core.client.transport.TLoadBalancerClient;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.BaseKeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.thrift.TServiceClient;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.transport.THttpClient;
import org.apache.thrift.transport.TTransport;
import org.springframework.beans.BeanUtils;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;

/**
 * Created by 迹_Jason on 2017/08/09.
 */
public class ThriftPooledObjectFactory extends BaseKeyedPooledObjectFactory<ThriftKey, TServiceClient> {

    private TProtocolFactory protocolFactory;
    private LoadBalancerClient loadBalancerClient;

    private ThriftProperty thriftProperty;

    public void setProtocolFactory(TProtocolFactory protocolFactory) {
        this.protocolFactory = protocolFactory;
    }

    public void setLoadBalancerClient(LoadBalancerClient loadBalancerClient) {
        this.loadBalancerClient = loadBalancerClient;
    }

    public void setThriftProperty(ThriftProperty thriftProperty) {
        this.thriftProperty = thriftProperty;
    }

    @Override
    public void activateObject(ThriftKey key, PooledObject<TServiceClient> p) throws Exception {
        super.activateObject(key, p);
    }

    @Override
    public void passivateObject(ThriftKey key, PooledObject<TServiceClient> p) throws Exception {
        DefaultPooledObject<TServiceClient> pooledObject = (DefaultPooledObject<TServiceClient>) p;
        TTransport transport = pooledObject.getObject().getOutputProtocol().getTransport();
        if (transport instanceof THttpClient) {
            ((THttpClient) transport).setCustomHeaders(null);
        } else {
            ((TLoadBalancerClient) transport).setCustomHeaders(null);
        }
        resetAndClose(p);

        super.passivateObject(key, p);
    }

    @Override
    public TServiceClient create(ThriftKey thriftKey) throws Exception {
        String serviceName = thriftKey.getServiceName();

        TProtocol protocol;
        if (StringUtils.isEmpty(thriftProperty.getService().getEndpoint())) {
            final TLoadBalancerClient loadBalancerClient = new TLoadBalancerClient(
                    this.loadBalancerClient,
                    serviceName,
                    thriftProperty.getService().getPath() + thriftKey.getPath()
            );
            loadBalancerClient.setConnectTimeout(thriftProperty.getService().getConnectTimeout());
            loadBalancerClient.setReadTimeout(thriftProperty.getService().getReadTimeout());
            loadBalancerClient.setMaxRetries(thriftProperty.getService().getMaxRetries());
            protocol = protocolFactory.getProtocol(loadBalancerClient);
        } else {
            final THttpClient httpClient = new THttpClient(thriftProperty.getService().getEndpoint());
            httpClient.setConnectTimeout(thriftProperty.getService().getConnectTimeout());
            httpClient.setReadTimeout(thriftProperty.getService().getReadTimeout());
            protocol = protocolFactory.getProtocol(httpClient);
        }

        return BeanUtils.instantiateClass(thriftKey.getClazz().getConstructor(TProtocol.class), (TProtocol) protocol);
    }

    @Override
    public PooledObject<TServiceClient> wrap(TServiceClient tServiceClient) {
        return new DefaultPooledObject<>(tServiceClient);
    }

    private void resetAndClose(PooledObject<TServiceClient> p) {
        p.getObject().getInputProtocol().reset();
        p.getObject().getOutputProtocol().reset();
        p.getObject().getInputProtocol().getTransport().close();
        p.getObject().getOutputProtocol().getTransport().close();
    }
}