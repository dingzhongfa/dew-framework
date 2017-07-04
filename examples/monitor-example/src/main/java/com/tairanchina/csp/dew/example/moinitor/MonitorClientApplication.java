package com.tairanchina.csp.dew.example.moinitor;

import com.tairanchina.csp.dew.core.DewCloudApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * 工程启动类
 */
public class MonitorClientApplication extends DewCloudApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(MonitorClientApplication.class).run(args);
    }

}
