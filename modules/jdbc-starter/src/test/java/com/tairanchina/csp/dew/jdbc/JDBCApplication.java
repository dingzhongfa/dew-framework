package com.tairanchina.csp.dew.jdbc;


import com.tairanchina.csp.dew.core.DewBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackageClasses = DewJDBCAutoConfiguration.class)
public class JDBCApplication extends DewBootApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(JDBCApplication.class).run(args);
    }
}
