package com.tairanchina.csp.dew.core.test;

import com.tairanchina.csp.dew.core.autoconfigure.DewBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@DewBootApplication
public class BootTestApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(BootTestApplication.class).web(true).run(args);
    }


}