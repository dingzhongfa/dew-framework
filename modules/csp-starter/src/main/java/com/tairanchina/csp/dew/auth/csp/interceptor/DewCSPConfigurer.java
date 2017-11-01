package com.tairanchina.csp.dew.auth.csp.interceptor;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@ConditionalOnWebApplication
@Order(30000)
public class DewCSPConfigurer extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        DewCSPHandlerInterceptor dewCSPHandlerInterceptor = new DewCSPHandlerInterceptor();
        registry.addInterceptor(dewCSPHandlerInterceptor).excludePathPatterns("/error/**");
        super.addInterceptors(registry);
    }

}
