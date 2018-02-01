package com.tairanchina.csp.dew.auth.csp.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.PostConstruct;

@Configuration
@ConditionalOnWebApplication
@Order(30000)
public class DewCSPWebMvcConfigurer extends WebMvcConfigurerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(DewCSPWebMvcConfigurer.class);

    @PostConstruct
    private void init(){
        logger.info("Load Auto Configuration : {}", this.getClass().getName());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        DewCSPHandlerInterceptor dewCSPHandlerInterceptor = new DewCSPHandlerInterceptor();
        registry.addInterceptor(dewCSPHandlerInterceptor).excludePathPatterns("/error/**");
        super.addInterceptors(registry);
    }

}
