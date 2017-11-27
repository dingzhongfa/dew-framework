package com.tairanchina.csp.dew.example.dubbo;

import com.tairanchina.csp.dew.core.autoconfigure.DewBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@DewBootApplication
public class DubboExampleApplication{

    public static void main(String[] args) {
        new SpringApplicationBuilder(DubboExampleApplication.class).run(args);
    }

}
