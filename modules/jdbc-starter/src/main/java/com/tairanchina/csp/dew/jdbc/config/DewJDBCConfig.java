package com.tairanchina.csp.dew.jdbc.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "dew")
public class DewJDBCConfig {

    private jdbc jdbc = new jdbc();

    public static class jdbc {

        private List<String> basePackages = new ArrayList<>();

        public List<String> getBasePackages() {
            return basePackages;
        }

        public void setBasePackages(List<String> basePackages) {
            this.basePackages = basePackages;
        }
    }

    public jdbc getJdbc() {
        return jdbc;
    }

    public void setJdbc(jdbc jdbc) {
        this.jdbc = jdbc;
    }

}
