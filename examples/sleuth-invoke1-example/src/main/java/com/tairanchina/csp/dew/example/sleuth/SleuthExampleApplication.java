package com.tairanchina.csp.dew.example.sleuth;

import ch.qos.logback.classic.PatternLayout;
import com.tairanchina.csp.dew.core.DewCloudApplication;
import com.tairanchina.csp.dew.example.sleuth.logger.TestConverter;
import org.springframework.boot.builder.SpringApplicationBuilder;

public class SleuthExampleApplication extends DewCloudApplication {

    public static void main(String[] args) {
        PatternLayout.defaultConverterMap.put("dew", TestConverter.class.getName());
        new SpringApplicationBuilder(SleuthExampleApplication.class).run(args);
    }

}
