package com.tairanchina.csp.dew.example.config;

import com.tairanchina.csp.dew.core.DewCloudApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

public class ConfigExampleApplication extends DewCloudApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ConfigExampleApplication.class).web(true).run(args);
    }

}
