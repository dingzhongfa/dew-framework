package com.tairanchina.csp.dew.core.test;

import com.tairanchina.csp.dew.core.DewConfig;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication(scanBasePackageClasses = DewConfig.class)
public class BootTestApplicationWithAnnotation {

    public static void main(String[] args) {
        new SpringApplicationBuilder(BootTestApplicationWithAnnotation.class).web(true).run(args);
    }

}