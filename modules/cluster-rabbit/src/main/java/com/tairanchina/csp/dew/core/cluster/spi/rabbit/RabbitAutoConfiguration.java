package com.tairanchina.csp.dew.core.cluster.spi.rabbit;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * desription:
 * Created by ding on 2018/1/25.
 */
@Configuration
public class RabbitAutoConfiguration {

    @Bean
    @ConditionalOnExpression("'${dew.cluster.mq}'=='rabbit'")
    @SuppressWarnings("SpringJavaAutowiringInspection")
    public RabbitAdapter rabbitAdapter(RabbitTemplate rabbitTemplate) {
        return new RabbitAdapter(rabbitTemplate);
    }

    @Bean
    @ConditionalOnBean(RabbitAdapter.class)
    public RabbitClusterMQ rabbitClusterMQ(RabbitAdapter rabbitAdapter) {
        return new RabbitClusterMQ(rabbitAdapter);
    }
}
