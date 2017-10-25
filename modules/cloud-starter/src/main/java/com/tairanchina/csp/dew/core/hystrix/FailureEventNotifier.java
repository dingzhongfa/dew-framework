package com.tairanchina.csp.dew.core.hystrix;

import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixEventType;
import com.netflix.hystrix.strategy.eventnotifier.HystrixEventNotifier;
import com.tairanchina.csp.dew.core.DewCloudConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FailureEventNotifier extends HystrixEventNotifier {

    @Autowired
    private DewCloudConfig dewCloudConfig;

    @Override
    public void markEvent(HystrixEventType eventType, HystrixCommandKey key) {
        if(dewCloudConfig.getError().getNotifyExcludeKeys().contains(key.name())){
            return;
        }
        if(!dewCloudConfig.getError().getNotifyIncludeKeys().isEmpty()
                && !dewCloudConfig.getError().getNotifyIncludeKeys().contains(key.name())){
            return;
        }
        if(!dewCloudConfig.getError().getNotifyEventTypes().contains(eventType.name())){
            return;
        }
        super.markEvent(eventType, key);
    }
}
