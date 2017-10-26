package com.tairanchina.csp.dew.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;


@SpringCloudApplication
public abstract class DewCloudApplication extends DewBootApplication {

    @Autowired
    private RestTemplate restTemplate;

    @Bean
    @LoadBalanced
    protected RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
