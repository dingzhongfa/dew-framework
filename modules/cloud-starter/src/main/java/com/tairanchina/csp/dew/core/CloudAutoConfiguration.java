package com.tairanchina.csp.dew.core;

import ch.qos.logback.classic.Level;
import com.netflix.hystrix.strategy.HystrixPlugins;
import com.tairanchina.csp.dew.core.hystrix.FailureEventNotifier;
import com.tairanchina.csp.dew.core.logger.DewLoggerWebMvcConfigurer;
import com.tairanchina.csp.dew.core.logger.DewTraceLogWrap;
import com.tairanchina.csp.dew.core.logger.DewTraceRestTemplateInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@Configuration
@EnableConfigurationProperties(DewCloudConfig.class)
public class CloudAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(CloudAutoConfiguration.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${spring.mail.username}")
    private String emailFrom;

    @Value("${spring.application.name:dew-default}")
    private String applicationName;

    @Value(("${spring.profiles.active:default}"))
    private String profile;

    @Autowired
    private DewCloudConfig dewCloudConfig;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired(required = false)
    private DewLoggerWebMvcConfigurer dewLoggerWebMvcConfigurer;


    public CloudAutoConfiguration(DewCloudConfig dewCloudConfig) {
        this.dewCloudConfig = dewCloudConfig;
    }

    @PostConstruct
    public void init() {
        logger.info("Load Auto Configuration : {}", this.getClass().getName());
        if (!dewCloudConfig.getError().getNotifyEmails().isEmpty() && dewCloudConfig.getError().isEnabled()) {
            logger.info("Enabled Failure Event Notifier");
            HystrixPlugins.getInstance().registerEventNotifier(new FailureEventNotifier(dewCloudConfig,mailSender,emailFrom,applicationName,profile));
        }
        if (dewCloudConfig.getTraceLog().isEnabled()) {
            logger.info("Enabled Trace Log");
            restTemplate.getInterceptors().add(new DewTraceRestTemplateInterceptor());
            ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(DewTraceLogWrap.class);
            root.setLevel(Level.TRACE);
        }
    }

}