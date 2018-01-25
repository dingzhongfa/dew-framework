package com.tairanchina.csp.dew.example.auth;

import com.tairanchina.csp.dew.core.Dew;
import com.tairanchina.csp.dew.core.autoconfigure.DewBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * 工程启动类
 */
@DewBootApplication(scanBasePackageClasses = {Dew.class,AuthExampleApplication.class})
public class AuthExampleApplication{

    public static void main(String[] args) {
        new SpringApplicationBuilder(AuthExampleApplication.class).run(args);
    }

}
