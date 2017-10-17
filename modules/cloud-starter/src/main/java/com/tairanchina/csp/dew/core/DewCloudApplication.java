package com.tairanchina.csp.dew.core;

import com.tairanchina.csp.dew.Dew;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@SpringCloudApplication
@ComponentScan(basePackageClasses = {Dew.class, DewCloudApplication.class})
public abstract class DewCloudApplication extends DewBootApplication {

    @Autowired
    private RestTemplate restTemplate;

    @Bean
    @LoadBalanced
    protected RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
