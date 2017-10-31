package com.tairanchina.csp.dew.core;

import com.netflix.hystrix.strategy.HystrixPlugins;
import com.netflix.hystrix.strategy.eventnotifier.HystrixEventNotifier;
import com.tairanchina.csp.dew.core.logger.DewLoggerConfigurer;
import com.tairanchina.csp.dew.core.logger.DewTraceRestTemplateInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Configuration
public class CloudAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(CloudAutoConfiguration.class);

    @Autowired
    private DewCloudConfig dewCloudConfig;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired(required = false)
    private DewLoggerConfigurer dewLoggerConfigurer;
    @Autowired(required = false)
    private HystrixEventNotifier hystrixEventNotifier;

    @PostConstruct
    public void init() throws IOException {
        if (!dewCloudConfig.getError().getNotifyEmails().isEmpty() && hystrixEventNotifier != null) {
            logger.info("Enabled Failure Event Notifier");
            HystrixPlugins.getInstance().registerEventNotifier(hystrixEventNotifier);
        }
        if (dewCloudConfig.getTraceLog().isEnabled()) {
            logger.info("Enabled Trace Log");
            restTemplate.getInterceptors().add(new DewTraceRestTemplateInterceptor());
        }
    }

}