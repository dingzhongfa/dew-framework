package com.tairanchina.csp.dew.example.hystrix;

import com.tairanchina.csp.dew.core.DewCloudApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

/**
 * 工程启动类
 */
@EnableFeignClients
public class HystrixFeignExampleApplication extends DewCloudApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(HystrixFeignExampleApplication.class).run(args);
    }

}
