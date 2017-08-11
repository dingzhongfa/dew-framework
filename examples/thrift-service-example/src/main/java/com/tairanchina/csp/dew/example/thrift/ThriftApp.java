package com.tairanchina.csp.dew.example.thrift;

import com.tairanchina.csp.dew.core.DewCloudApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by 迹_Jason on 2017/08/09.
 */
public class ThriftApp extends DewCloudApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ThriftApp.class).run(args);
    }

    @RequestMapping("/")
    public String home() {
        return "Hello World";
    }
}