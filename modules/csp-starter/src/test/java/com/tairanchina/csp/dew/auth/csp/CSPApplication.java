package com.tairanchina.csp.dew.auth.csp;

import com.tairanchina.csp.dew.core.autoconfigure.DewBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * desription:
 * Created by ding on 2017/10/30.
 */
@DewBootApplication
public class CSPApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(CSPApplication.class).run(args);
    }
}
