package com.tairanchina.csp.dew.core.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * desription:
 * Created by ding on 2018/1/25.
 */
@Configuration
public class AuthAutoConfiguration {

    @Bean
    public BasicAuthAdapter basicAuthAdapter(){
        return new BasicAuthAdapter();
    }
}
