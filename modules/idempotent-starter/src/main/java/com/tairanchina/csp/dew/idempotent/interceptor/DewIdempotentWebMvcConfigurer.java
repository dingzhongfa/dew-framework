package com.tairanchina.csp.dew.idempotent.interceptor;

import com.tairanchina.csp.dew.idempotent.DewIdempotentConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@ConditionalOnWebApplication
@EnableConfigurationProperties(DewIdempotentConfig.class)
@Order(10000)
public class DewIdempotentWebMvcConfigurer extends WebMvcConfigurerAdapter {

    private DewIdempotentConfig dewIdempotentConfig;


    public DewIdempotentWebMvcConfigurer(DewIdempotentConfig dewIdempotentConfig) {
        this.dewIdempotentConfig = dewIdempotentConfig;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new DewIdempotentHandlerInterceptor(dewIdempotentConfig)).excludePathPatterns("/error/**");
        super.addInterceptors(registry);
    }

}
