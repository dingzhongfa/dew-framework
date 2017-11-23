package com.tairanchina.csp.dew.example.idempotent;


import com.tairanchina.csp.dew.core.DewBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

public class IdempotentExampleApplication extends DewBootApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(IdempotentExampleApplication.class).run(args);
    }
}
