package com.tairanchina.csp.dew.auth.csp;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * desription:
 * Created by ding on 2017/10/30.
 */
@SpringBootApplication
public class CSPApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(CSPApplication.class).run(args);
    }
}
