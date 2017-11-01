package com.tairanchina.csp.dew.core.test;

import com.tairanchina.csp.dew.core.DewBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.util.Date;

public class BootTestApplication extends DewBootApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(BootTestApplication.class).web(true).run(args);
    }


}