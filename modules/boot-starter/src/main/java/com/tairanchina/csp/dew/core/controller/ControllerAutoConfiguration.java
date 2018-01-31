package com.tairanchina.csp.dew.core.controller;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.Servlet;
import java.util.List;

/**
 * desription:
 * Created by ding on 2018/1/26.
 */
@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass({ Servlet.class, DispatcherServlet.class })
@AutoConfigureBefore(ErrorMvcAutoConfiguration.class)
// Load before the main WebMvcAutoConfiguration so that the error View is available
/*@AutoConfigureBefore(WebMvcAutoConfiguration.class)
@EnableConfigurationProperties(ServerProperties.class)*/
public class ControllerAutoConfiguration {

   /* private final ServerProperties serverProperties;

    private final List<ErrorViewResolver> errorViewResolvers;

    public ControllerAutoConfiguration(ServerProperties serverProperties,
                                     ObjectProvider<List<ErrorViewResolver>> errorViewResolversProvider) {
        this.serverProperties = serverProperties;
        this.errorViewResolvers = errorViewResolversProvider.getIfAvailable();
    }*/

    @Bean
    @SuppressWarnings("SpringJavaAutowiringInspection")
    public ErrorController errorController(ErrorAttributes errorAttributes){
        return new ErrorController(errorAttributes);
    }
}
