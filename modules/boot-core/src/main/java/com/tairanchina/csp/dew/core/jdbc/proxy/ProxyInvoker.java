package com.tairanchina.csp.dew.core.jdbc.proxy;

import org.springframework.cglib.proxy.Proxy;

/**
 * Created by 迹_Jason on 2017/7/26.
 */
public class ProxyInvoker {

    public Object getInstance(Class<?> cls) {
        return Proxy.newProxyInstance(
                cls.getClassLoader(),
                new Class[]{cls},
                new MethodProxy());
    }

}