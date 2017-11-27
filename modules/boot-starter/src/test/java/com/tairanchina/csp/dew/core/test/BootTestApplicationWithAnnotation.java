package com.tairanchina.csp.dew.core.test;

import com.tairanchina.csp.dew.Dew;
import com.tairanchina.csp.dew.core.autoconfigure.DewBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@DewBootApplication(scanBasePackageClasses = {Dew.class,BootTestApplicationWithAnnotation.class})
public class BootTestApplicationWithAnnotation {

    public static void main(String[] args) {
        new SpringApplicationBuilder(BootTestApplicationWithAnnotation.class).web(true).run(args);
    }

}