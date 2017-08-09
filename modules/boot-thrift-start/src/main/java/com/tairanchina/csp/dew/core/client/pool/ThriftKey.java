package com.tairanchina.csp.dew.core.client.pool;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.thrift.TServiceClient;

/**
 * Created by 迹_Jason on 2017/08/09.
 */
public class ThriftKey {

    private Class<? extends TServiceClient> clazz;
    private String serviceName;
    private String path;

    public ThriftKey(Class<? extends TServiceClient> clazz) {
        this.clazz = clazz;
    }

    public Class<? extends TServiceClient> getClazz() {
        return clazz;
    }

    public String getServiceName() {
        if (StringUtils.isEmpty(serviceName)) {
            return WordUtils.uncapitalize(clazz.getEnclosingClass().getSimpleName());
        }
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}