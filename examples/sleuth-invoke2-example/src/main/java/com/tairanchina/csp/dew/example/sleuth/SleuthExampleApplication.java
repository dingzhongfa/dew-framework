package com.tairanchina.csp.dew.example.sleuth;

import com.tairanchina.csp.dew.core.DewCloudApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.builder.SpringApplicationBuilder;

public class SleuthExampleApplication extends DewCloudApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(SleuthExampleApplication.class).run(args);
    }

}
