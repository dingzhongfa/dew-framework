package com.tairanchina.csp.dew.gateway;

import com.tairanchina.csp.dew.core.Dew;
import com.tairanchina.csp.dew.core.DewCloudApplication;
import com.tairanchina.csp.dew.gateway.auth.LoggingFilter;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.netflix.zuul.filters.discovery.PatternServiceRouteMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@EnableZuulProxy
@ComponentScan(basePackageClasses = {Dew.class, GatewayApplication.class})
public class GatewayApplication extends DewCloudApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(GatewayApplication.class).web(true).run(args);
    }

    public LoggingFilter loggingFilter() {
        return new LoggingFilter();
    }

    @Bean
    public PatternServiceRouteMapper serviceRouteMapper() {
        return new PatternServiceRouteMapper("(?<name>^.+)", "${name}");
    }

}
