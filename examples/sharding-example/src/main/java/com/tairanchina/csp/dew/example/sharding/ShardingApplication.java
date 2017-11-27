package com.tairanchina.csp.dew.example.sharding;


import com.tairanchina.csp.dew.core.autoconfigure.DewBootApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;

@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
@DewBootApplication
public class ShardingApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ShardingApplication.class).run(args);
    }
}
