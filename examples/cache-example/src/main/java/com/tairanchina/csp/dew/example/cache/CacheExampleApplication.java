package com.tairanchina.csp.dew.example.cache;

import com.tairanchina.csp.dew.core.DewBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * 工程启动类
 */
public class CacheExampleApplication extends DewBootApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(CacheExampleApplication.class).run(args);
    }

}
