package com.tairanchina.csp.dew.core.test.postman;

import com.tairanchina.csp.dew.core.DewBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.util.Date;

public class TestStartup extends DewBootApplication {

    public static void main(String[] args) {
        long start = new Date().getTime();
        new SpringApplicationBuilder(TestStartup.class).web(true).run(args);
        System.out.println(">>>>>>>>>>>>"+(new Date().getTime() - start));
    }

}