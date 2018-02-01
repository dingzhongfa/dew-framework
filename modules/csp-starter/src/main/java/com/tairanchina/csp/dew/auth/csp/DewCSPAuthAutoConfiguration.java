package com.tairanchina.csp.dew.auth.csp;

import com.tairanchina.csp.dew.Dew;
import com.tairanchina.csp.dew.core.DewContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@EnableConfigurationProperties(DewCSPConfig.class)
@AutoConfigureAfter(Dew.class)
public class DewCSPAuthAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(DewCSPAuthAutoConfiguration.class);

    public static DewCSPConfig dewCSPConfig;

    private DewCSPConfig cSPConfig;

    public DewCSPAuthAutoConfiguration(DewCSPConfig cSPConfig) {

        this.cSPConfig = cSPConfig;
    }

    @PostConstruct
    private void init() {
        logger.info("Load Auto Configuration : {}", this.getClass().getName());
        logger.info("Enabled Dew CSP Auth");
        Dew.auth = new CSPAuthAdapter();
        DewContext.setOptInfoClazz(CSPOptInfo.class);
        dewCSPConfig = cSPConfig;
    }

}