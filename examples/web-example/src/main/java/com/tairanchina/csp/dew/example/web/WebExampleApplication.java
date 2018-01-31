package com.tairanchina.csp.dew.example.web;

import com.tairanchina.csp.dew.core.Dew;
import com.tairanchina.csp.dew.core.autoconfigure.DewBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * 工程启动类
 */
@DewBootApplication(scanBasePackageClasses = {Dew.class,WebExampleApplication.class})
public class WebExampleApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(WebExampleApplication.class).run(args);
    }

}
