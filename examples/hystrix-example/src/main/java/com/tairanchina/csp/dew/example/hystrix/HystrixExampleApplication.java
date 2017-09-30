package com.tairanchina.csp.dew.example.hystrix;

import com.tairanchina.csp.dew.core.Dew;
import com.tairanchina.csp.dew.core.DewCloudApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

/**
 * 工程启动类
 */
@ComponentScan(basePackageClasses = {Dew.class, HystrixExampleApplication.class})
public class HystrixExampleApplication extends DewCloudApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(HystrixExampleApplication.class).run(args);
    }

}
