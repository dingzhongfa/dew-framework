package com.tairanchina.csp.dew.example.cluster;

import com.tairanchina.csp.dew.core.DewBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * 工程启动类
 */
public class ClusterExampleApplication extends DewBootApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ClusterExampleApplication.class).run(args);
    }

}
