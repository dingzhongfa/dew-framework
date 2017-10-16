package com.tairanchina.csp.dew.example.mybatisplus;

import com.tairanchina.csp.dew.Dew;
import com.tairanchina.csp.dew.core.DewBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackageClasses = {Dew.class,MybatisplusExampleApplication.class})
public class MybatisplusExampleApplication extends DewBootApplication {

    public static void main(String[] args) throws InterruptedException {
        new SpringApplicationBuilder(MybatisplusExampleApplication.class).run(args);
    }
}
