package com.tairanchina.csp.dew.jdbc.sharding.transaction;

import com.tairanchina.csp.dew.jdbc.DewDS;
import com.tairanchina.csp.dew.jdbc.sharding.ShardingEnvironmentAware;
import io.shardingjdbc.transaction.api.SoftTransactionManager;
import io.shardingjdbc.transaction.api.config.SoftTransactionConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;

/**
 * desription:
 * Created by ding on 2017/12/13.
 */
@Configuration
@ConditionalOnBean(ShardingEnvironmentAware.class)
public class ShardingTransactionConfiguration {

    @Value("${sharding.transaction.name:transaction}")
    private String transaction;

    private static final Logger logger = LoggerFactory.getLogger(ShardingTransactionConfiguration.class);


    @Bean
    @SuppressWarnings("SpringJavaAutowiringInspection")
    public SoftTransactionManager softTransactionManager(ApplicationContext applicationContext, ShardingEnvironmentAware shardingEnvironmentAware) throws Exception {
        SoftTransactionConfiguration softTransactionConfiguration = new SoftTransactionConfiguration(shardingEnvironmentAware.dataSource());
        softTransactionConfiguration.setTransactionLogDataSource(((DewDS) applicationContext.getBean(transaction + "DS")).jdbc().getDataSource());
        SoftTransactionManager softTransactionManager = new SoftTransactionManager(softTransactionConfiguration);
        try {
            softTransactionManager.init();
        } catch (SQLException e) {
            logger.error("Dew error : softTransactionManager init failed ");
        }
        return softTransactionManager;
    }


}