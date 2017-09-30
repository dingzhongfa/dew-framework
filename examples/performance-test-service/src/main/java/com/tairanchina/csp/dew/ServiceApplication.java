package com.tairanchina.csp.dew;

import org.springframework.boot.builder.SpringApplicationBuilder;

import com.tairanchina.csp.dew.core.DewCloudApplication;

/**
 * ServiceApplication
 *
 * @author hzzjb
 * @date 2017/9/19
 */
public class ServiceApplication extends DewCloudApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(ServiceApplication.class).run(args);
    }
}
