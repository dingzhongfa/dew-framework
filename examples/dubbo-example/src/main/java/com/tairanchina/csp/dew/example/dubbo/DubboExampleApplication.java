package com.tairanchina.csp.dew.example.dubbo;

import com.tairanchina.csp.dew.core.Dew;
import com.tairanchina.csp.dew.core.autoconfigure.DewBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@DewBootApplication(scanBasePackageClasses = {Dew.class,DubboExampleApplication.class})
public class DubboExampleApplication{

    public static void main(String[] args) {
        new SpringApplicationBuilder(DubboExampleApplication.class).run(args);
    }

}
