package com.tairanchina.csp.dew.example.thrift;

import com.tairanchina.csp.dew.core.DewCloudApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

public class ThriftExampleApplication extends DewCloudApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ThriftExampleApplication.class).run(args);
    }

}