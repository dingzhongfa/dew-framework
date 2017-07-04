package com.tairanchina.csp.dew.core.cluster.spi.rabbit;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnExpression("#{'${dew.cluster.mq}'=='rabbit'}")
public class RabbitAdapter {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    AmqpTemplate getRabbitTemplate() {
        return rabbitTemplate;
    }

    Connection getConnection() {
        return rabbitTemplate.getConnectionFactory().createConnection();
    }

}
