package com.tairanchina.csp.dew;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

import com.tairanchina.csp.dew.core.DewCloudApplication;

/**
 * ApiApplication
 *
 * @author hzzjb
 * @date 2017/9/27
 */
@EnableFeignClients
@EnableDiscoveryClient
public class ApiApplication extends DewCloudApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ApiApplication.class).run(args);
    }
}
