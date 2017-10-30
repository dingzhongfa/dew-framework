package com.tairanchina.csp.dew.auth.csp;

import com.tairanchina.csp.dew.auth.csp.DewCSPAuthAutoConfiguration;
import com.tairanchina.csp.dew.core.DewBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

/**
 * desription:
 * Created by ding on 2017/10/30.
 */
@ComponentScan(basePackageClasses = DewCSPAuthAutoConfiguration.class)
public class CSPApplication extends DewBootApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(CSPApplication.class).run(args);
    }
}
