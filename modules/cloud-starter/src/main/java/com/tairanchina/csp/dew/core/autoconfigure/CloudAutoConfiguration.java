package com.tairanchina.csp.dew.core.autoconfigure;

import ch.qos.logback.classic.Level;
import com.netflix.hystrix.strategy.HystrixPlugins;
import com.netflix.hystrix.strategy.eventnotifier.HystrixEventNotifier;
import com.tairanchina.csp.dew.core.DewCloudConfig;
import com.tairanchina.csp.dew.core.hystrix.FailureEventNotifier;
import com.tairanchina.csp.dew.core.loding.DewLoadImmediately;
import com.tairanchina.csp.dew.core.logger.DewLoggerWebMvcConfigurer;
import com.tairanchina.csp.dew.core.logger.DewTraceLogWrap;
import com.tairanchina.csp.dew.core.logger.DewTraceRestTemplateInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@Configuration
@EnableConfigurationProperties(DewCloudConfig.class)
@DewLoadImmediately
public class CloudAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(CloudAutoConfiguration.class);

    private DewCloudConfig dewCloudConfig;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired(required = false)
    private DewLoggerWebMvcConfigurer dewLoggerWebMvcConfigurer;


    public CloudAutoConfiguration(DewCloudConfig dewCloudConfig) {
        this.dewCloudConfig = dewCloudConfig;
    }

    @Bean
    @LoadBalanced
    @ConditionalOnMissingBean
    protected RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @PostConstruct
    public void init() {
        if (!dewCloudConfig.getError().getNotifyEmails().isEmpty() && dewCloudConfig.getError().isEnabled()) {
            logger.info("Enabled Failure Event Notifier");
            HystrixPlugins.getInstance().registerEventNotifier(new FailureEventNotifier());
        }
        if (dewCloudConfig.getTraceLog().isEnabled()) {
            logger.info("Enabled Trace Log");
            restTemplate.getInterceptors().add(new DewTraceRestTemplateInterceptor());
            ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(DewTraceLogWrap.class);
            root.setLevel(Level.TRACE);
        }
    }

}