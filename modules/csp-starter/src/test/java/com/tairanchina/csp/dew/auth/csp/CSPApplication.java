package com.tairanchina.csp.dew.auth.csp;

import com.tairanchina.csp.dew.core.Dew;
import com.tairanchina.csp.dew.core.autoconfigure.DewBootApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.SpringBootTest;

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
