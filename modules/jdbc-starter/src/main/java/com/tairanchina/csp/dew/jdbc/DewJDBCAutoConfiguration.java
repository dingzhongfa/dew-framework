package com.tairanchina.csp.dew.jdbc;

import com.tairanchina.csp.dew.core.Dew;
import com.tairanchina.csp.dew.jdbc.entity.EntityContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

// @Configuration
@Component
public class DewJDBCAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(DewJDBCAutoConfiguration.class);

    @Autowired
    private DewJDBCConfig dewJDBCConfig;

    @PostConstruct
    private void init(){
        logger.info("Enabled Dew JDBC");
        Dew.applicationContext.containsBean(EntityContainer.class.getSimpleName());
        // JDBC Scan
        if (!dewJDBCConfig.getJdbc().getBasePackages().isEmpty()) {
            ClassPathScanner scanner = new ClassPathScanner((BeanDefinitionRegistry) ((GenericApplicationContext) Dew.applicationContext).getBeanFactory());
            scanner.setResourceLoader(Dew.applicationContext);
            scanner.registerFilters();
            scanner.scan(dewJDBCConfig.getJdbc().getBasePackages().toArray(new String[]{}));
        }
    }


}