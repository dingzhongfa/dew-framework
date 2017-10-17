package com.tairanchina.csp.dew.example.auth;

import com.tairanchina.csp.dew.core.DewBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * 工程启动类
 */
public class AuthExampleApplication extends DewBootApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(AuthExampleApplication.class).run(args);
    }

}
