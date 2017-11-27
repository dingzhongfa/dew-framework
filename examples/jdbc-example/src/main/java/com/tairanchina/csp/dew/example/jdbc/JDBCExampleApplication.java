package com.tairanchina.csp.dew.example.jdbc;

import com.tairanchina.csp.dew.core.autoconfigure.DewBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * 工程启动类
 */
@DewBootApplication
public class JDBCExampleApplication {

    public static void main(String[] args) throws InterruptedException {
        new SpringApplicationBuilder(JDBCExampleApplication.class).run(args);
    }

}
