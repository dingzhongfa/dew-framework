package com.tairanchina.csp.dew.idempotent;


import com.tairanchina.csp.dew.Dew;
import com.tairanchina.csp.dew.core.autoconfigure.DewBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@DewBootApplication(scanBasePackageClasses = {Dew.class, IdempotentApplication.class})
public class IdempotentApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(IdempotentApplication.class).run(args);
    }
}
