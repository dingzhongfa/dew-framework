package com.tairanchina.csp.dew.auth.csp;

import com.tairanchina.csp.dew.Dew;
import com.tairanchina.csp.dew.core.DewContext;
import com.tairanchina.csp.dew.core.loding.DewLoadImmediately;
import com.tairanchina.csp.foundation.sdk.CSPKernelSDK;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@DewLoadImmediately
public class DewCSPAuthAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(DewCSPAuthAutoConfiguration.class);

    @PostConstruct
    private void init() {
        logger.info("Enabled Dew UC Auth");
        Dew.auth = Dew.applicationContext.getBean(CSPAuthAdapter.class);
        DewContext.setOptInfoClazz(CSPOptInfo.class);
    }

}