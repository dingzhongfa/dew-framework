package com.tairanchina.csp.dew.example.cache;

import com.tairanchina.csp.dew.core.Dew;
import com.tairanchina.csp.dew.core.DewBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

/**
 * 工程启动类
 */
@ComponentScan(basePackageClasses = {Dew.class,CacheExampleApplication.class})
public class CacheExampleApplication extends DewBootApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(CacheExampleApplication.class).run(args);
    }

}
