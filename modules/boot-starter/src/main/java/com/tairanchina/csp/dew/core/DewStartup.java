package com.tairanchina.csp.dew.core;

import com.tairanchina.csp.dew.Dew;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.jackson.JacksonProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.Order;

import javax.annotation.PostConstruct;
import javax.servlet.*;
import java.io.IOException;

@Configuration
@EnableConfigurationProperties(DewConfig.class)
@AutoConfigureOrder(Integer.MIN_VALUE)
public class DewStartup {

    private static final Logger logger = LoggerFactory.getLogger(DewStartup.class);

    @Value("${spring.application.name}")
    private String applicationName;

    private DewConfig dewConfig;

    public DewStartup(DewConfig dewConfig) {
        this.dewConfig = dewConfig;
    }

    @Autowired
    private Dew dew;

    @PostConstruct
    public void init() {
        logger.info("Load Auto Configuration : {}", this.getClass().getName());
    }

    @Bean
    public Filter dewStartupFilter() {
        return new DewStartupFilter();
    }

    @Bean
    public Dew dew(DewConfig dewConfig, ApplicationContext applicationContext, @Autowired(required = false) JacksonProperties jacksonProperties) throws IOException, ClassNotFoundException {
        return new Dew(applicationName, dewConfig, jacksonProperties, applicationContext);
    }

    @Order(Integer.MIN_VALUE)
    public class DewStartupFilter implements Filter {

        @Override
        public void init(FilterConfig filterConfig) throws ServletException {

        }

        @Override
        public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
            filterChain.doFilter(servletRequest,servletResponse);
        }

        @Override
        public void destroy() {

        }
    }
}
