package com.tairanchina.csp.dew.example.hystrixtwo;

import com.tairanchina.csp.dew.core.DewCloudApplication;
import feign.Retryer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

/**
 * desription:
 * Created by ding on 2017/11/17.
 */
@EnableFeignClients
public class HystrixExample2Application extends DewCloudApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(HystrixExample2Application.class).run(args);
    }

    @Bean
    public Retryer retryer(){
        return new DefaultRetryer();
    }
}
