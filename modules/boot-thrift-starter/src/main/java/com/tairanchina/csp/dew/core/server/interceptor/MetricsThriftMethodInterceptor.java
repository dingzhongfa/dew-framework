package com.tairanchina.csp.dew.core.server.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.boot.actuate.metrics.GaugeService;

/**
 * Created by 迹_Jason on 2017/08/09.
 */
public class MetricsThriftMethodInterceptor implements MethodInterceptor {

    private GaugeService gaugeService;
    private CounterService counterService;

    public MetricsThriftMethodInterceptor(GaugeService gaugeService, CounterService counterService) {
        this.gaugeService = gaugeService;
        this.counterService = counterService;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        long startTime = System.currentTimeMillis();
        try {
            return invocation.proceed();
        } finally {
            long endTime = System.currentTimeMillis();
            if (this.counterService != null) {
                counterService.increment(invocation.getMethod().getName());
            }
            if (this.gaugeService != null) {
                gaugeService.submit("timer.thrift." + invocation.getThis().getClass().getCanonicalName() + "." + invocation.getMethod().getName(), 0d + endTime - startTime);
            }
        }
    }
}