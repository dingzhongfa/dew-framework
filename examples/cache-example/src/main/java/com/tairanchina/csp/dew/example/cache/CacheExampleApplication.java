package com.tairanchina.csp.dew.example.cache;

import com.tairanchina.csp.dew.core.Dew;
import com.tairanchina.csp.dew.core.autoconfigure.DewBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * 工程启动类
 */
@DewBootApplication(scanBasePackageClasses = {Dew.class,CacheExampleApplication.class})
public class CacheExampleApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(CacheExampleApplication.class).run(args);
    }

}
