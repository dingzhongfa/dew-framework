package com.tairanchina.csp.dew.example.idempotent;


import com.tairanchina.csp.dew.Dew;
import com.tairanchina.csp.dew.core.autoconfigure.DewBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@DewBootApplication(scanBasePackageClasses = {Dew.class,IdempotentExampleApplication.class})
public class IdempotentExampleApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(IdempotentExampleApplication.class).run(args);
    }
}
