package com.tairanchina.csp.dew.example.rest;

import com.tairanchina.csp.dew.core.Dew;
import com.tairanchina.csp.dew.core.DewCloudApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

/**
 * 工程启动类
 */
@ComponentScan(basePackageClasses = {Dew.class, RestExampleApplication.class})
public class RestExampleApplication extends DewCloudApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(RestExampleApplication.class).run(args);
    }

}
