package com.tairanchina.csp.dew.example.mybatisplus;

import com.tairanchina.csp.dew.core.autoconfigure.DewBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@DewBootApplication
public class MybatisplusExampleApplication {

    public static void main(String[] args) throws InterruptedException {
        new SpringApplicationBuilder(MybatisplusExampleApplication.class).run(args);
    }
}
