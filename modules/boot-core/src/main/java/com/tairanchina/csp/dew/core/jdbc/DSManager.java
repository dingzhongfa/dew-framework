package com.tairanchina.csp.dew.core.jdbc;

import com.ecfront.dew.common.$;
import com.tairanchina.csp.dew.core.Dew;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.Map;

@Component
@ConditionalOnClass(JdbcTemplate.class)
public class DSManager {

    @Autowired
    private DSConfig dsConfig;

    @Autowired
    private JdbcTemplate primaryJdbcTemplate;

    @Value("${spring.datasource.url}")
    private String primaryJdbcUrl;

    private DefaultListableBeanFactory beanFactory;

    @PostConstruct
    private void init() throws NoSuchFieldException {
        beanFactory = (DefaultListableBeanFactory) ((ConfigurableApplicationContext) Dew.applicationContext).getBeanFactory();
        // Package primary DS
        AbstractBeanDefinition dsBean = BeanDefinitionBuilder.rootBeanDefinition(DS.class)
                .addPropertyValue("jdbcTemplate", primaryJdbcTemplate)
                .addPropertyValue("jdbcUrl", primaryJdbcUrl)
                .setInitMethodName("init").getBeanDefinition();
        dsBean.setScope("singleton");
        dsBean.setPrimary(true);
        beanFactory.registerBeanDefinition("ds", dsBean);
        // Package others DS
        if (dsConfig.getMultiDatasources() != null && !dsConfig.getMultiDatasources().isEmpty()) {
            for (Map.Entry<String, Map<String, String>> entry : dsConfig.getMultiDatasources().entrySet()) {
                String dsName = entry.getKey();
                DataSourceBuilder builder = DataSourceBuilder.create();
                $.bean.setValue(builder, "properties", entry.getValue());
                register(dsName, entry.getValue().get("url"), builder.build());
            }
        }
    }

    private void register(String dsName, String jdbcUrl, DataSource dataSource) {
        // Register JdbcTemplate
        AbstractBeanDefinition jdbcTemplate = BeanDefinitionBuilder.rootBeanDefinition(JdbcTemplate.class)
                .addConstructorArgValue(dataSource).getBeanDefinition();
        jdbcTemplate.setScope("singleton");
        jdbcTemplate.setPrimary(false);
        beanFactory.registerBeanDefinition(dsName + "JdbcTemplate", jdbcTemplate);
        // Register DS
        AbstractBeanDefinition dsBean = BeanDefinitionBuilder.rootBeanDefinition(DS.class)
                .addPropertyReference("jdbcTemplate", dsName + "JdbcTemplate")
                .addPropertyValue("jdbcUrl", jdbcUrl)
                .setInitMethodName("init").getBeanDefinition();
        dsBean.setScope("singleton");
        dsBean.setPrimary(false);
        beanFactory.registerBeanDefinition(dsName + "DS", dsBean);
    }

    public static DS select(String dsName) {
        if (dsName == null) {
            dsName = "";
        }
        if (dsName.isEmpty()) {
            return (DS) Dew.applicationContext.getBean("ds");
        } else {
            return (DS) Dew.applicationContext.getBean(dsName + "DS");
        }
    }

}

