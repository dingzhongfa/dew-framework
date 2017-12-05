package com.tairanchina.csp.dew.core.cluster.spi.kafka;

import com.tairanchina.csp.dew.core.cluster.ClusterMQ;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * desription:
 * Created by ding on 2017/11/9.
 */
@Component
@ConditionalOnBean(KafkaAdapter.class)
public class KafkaClusterMQ implements ClusterMQ {

    private String DEFAULT_TOPIC = "TRC_TOPIC";

    @Autowired
    private KafkaAdapter kafkaAdapter;

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    @Override
    public boolean publish(String topic, String message) {
        return publish(topic,message,false);
    }

    public boolean publish(String topic, String message,boolean confirm){
        try {
            if (confirm){
                kafkaAdapter.getKafkaProducer().send(new ProducerRecord<>(topic, message),new KafkaCallBack());
            }else {
                kafkaAdapter.getKafkaProducer().send(new ProducerRecord<>(topic, message));
            }
        } catch (Exception e) {
            logger.error("[MQ] Kafka response error.", e);
            return false;
        }
        return true;
    }

    @Override
    public void subscribe(String topic, Consumer<String> consumer) {
        new Thread(() -> {
            KafkaConsumer<String, String> kafkaConsumer = kafkaAdapter.getKafkaConsumer();
            kafkaConsumer.subscribe(Collections.singletonList(topic));
            while (true) {
                ConsumerRecords<String, String> records = kafkaConsumer.poll(1000);
                executorService.execute(() -> records.forEach(record -> consumer.accept(record.value())));
            }
        }).start();
    }

    @Override
    public boolean request(String partition, String message) {
        return request(partition,message,false);
    }

    public boolean request(String partition, String message,boolean confirm){
        try {
            if (confirm){
                kafkaAdapter.getKafkaProducer().send(new ProducerRecord<>(DEFAULT_TOPIC, Integer.valueOf(partition), null, message),new KafkaCallBack());
            }else {
                kafkaAdapter.getKafkaProducer().send(new ProducerRecord<>(DEFAULT_TOPIC, Integer.valueOf(partition), null, message));
            }

        } catch (Exception e) {
            logger.error("[MQ] Kafka response error.", e);
            return false;
        }
        return true;
    }

    @Override
    public void response(String partition, Consumer<String> consumer) {
        new Thread(() -> {
            KafkaConsumer<String, String> kafkaConsumer = kafkaAdapter.getKafkaConsumer();
            kafkaConsumer.subscribe(Collections.singletonList(DEFAULT_TOPIC));
            while (true) {
                ConsumerRecords<String, String> records = kafkaConsumer.poll(1000);
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        records.forEach(record -> {
                            if (record.partition() == Integer.valueOf(partition)) {
                                consumer.accept(record.value());
                            }
                        });
                    }
                });
            }
        }).start();
    }

    class KafkaCallBack implements Callback {
        @Override
        public void onCompletion(RecordMetadata metadata, Exception exception) {
           logger.warn("[MQ] Kafka send failed ",metadata,exception);
        }
    }

    public KafkaClusterMQ setKafkaAdapter(KafkaAdapter kafkaAdapter) {
        this.kafkaAdapter = kafkaAdapter;
        return this;
    }
}
