package com.tairanchina.csp.dew.example.dubbo;

import com.tairanchina.csp.dew.core.DewBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

public class DubboExampleApplication extends DewBootApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(DubboExampleApplication.class).run(args);
    }

}
