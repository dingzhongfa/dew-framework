package com.tairanchina.csp.dew.example.dubbo;

import com.tairanchina.csp.dew.Dew;
import com.tairanchina.csp.dew.core.DewBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackageClasses = {Dew.class,DubboExampleApplication.class})
public class DubboExampleApplication extends DewBootApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(DubboExampleApplication.class).run(args);
    }

}
