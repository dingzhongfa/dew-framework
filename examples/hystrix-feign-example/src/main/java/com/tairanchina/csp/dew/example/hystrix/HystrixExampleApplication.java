package com.tairanchina.csp.dew.example.hystrix;

import com.tairanchina.csp.dew.core.DewCloudApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 工程启动类
 */
@EnableFeignClients
public class HystrixExampleApplication extends DewCloudApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(HystrixExampleApplication.class).run(args);
    }

}
