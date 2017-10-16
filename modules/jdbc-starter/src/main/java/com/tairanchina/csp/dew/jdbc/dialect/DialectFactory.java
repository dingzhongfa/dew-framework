package com.tairanchina.csp.dew.jdbc.dialect;


public class DialectFactory {

    public static Dialect parseDialect(String url) {
        if (url.contains("ds:h2:")) {
            return new H2Dialect();
        } else if (url.contains("ds:mysql:")) {
            return new MySQLDialect();
        } else if (url.contains("ds:postgresql:")) {
            return new PostgresDialect();
        } else if (url.contains("ds:oracle:")) {
            return new OracleDialect();
        } else if (url.contains("ds:hive2:")) {
            return new HiveDialect();
        }
        return null;
    }

}
