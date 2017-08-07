package com.tairanchina.csp.dew.example.zipkin;

import com.tairanchina.csp.dew.core.DewBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * 工程启动类
 */
public class ZipkinExampleApplication extends DewBootApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ZipkinExampleApplication.class).run(args);
    }

}
