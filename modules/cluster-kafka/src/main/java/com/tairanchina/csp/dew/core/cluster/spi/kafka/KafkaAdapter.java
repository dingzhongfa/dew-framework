package com.tairanchina.csp.dew.core.cluster.spi.kafka;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.Properties;

/**
 * desription:
 * Created by ding on 2017/11/9.
 */
@Component
@ConditionalOnExpression("#{'${dew.cluster.mq}'=='kafka'}")
public class KafkaAdapter {

    private Properties producerProperties;

    private Properties consumerProperties;

    @PostConstruct
    public void init() throws IllegalAccessException {
        // 生产者（发布）
        setProducerProperties(getProperties(new KafkaProperties.Producer()));
        // 消费者（订阅）
        setConsumerProperties(getProperties(new KafkaProperties.Consumer()));
    }

    KafkaProducer<String, String> getKafkaProducer() {
        return new KafkaProducer<String, String>(producerProperties);
    }

    KafkaConsumer<String, String> getKafkaConsumer() {
        return new KafkaConsumer<String, String>(consumerProperties);
    }

    private void setProducerProperties(Properties producerProperties) {
        this.producerProperties = producerProperties;
    }

    private void setConsumerProperties(Properties consumerProperties) {
        this.consumerProperties = consumerProperties;
    }

    private Properties getProperties(Object obj) throws IllegalAccessException {
        Properties properties = new Properties();
        Field[] fields = obj.getClass().getFields();
        for (Field f : fields) {
            properties.put(fileNameToKey(f.getName()), f.get(obj));
        }
        return properties;
    }

    private String fileNameToKey(Object obj) {
        String param = String.valueOf(obj);
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append(".");
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
