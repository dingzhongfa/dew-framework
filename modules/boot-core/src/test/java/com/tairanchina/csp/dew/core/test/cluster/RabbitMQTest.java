package com.tairanchina.csp.dew.core.test.cluster;


import org.springframework.stereotype.Component;

@Component
public class RabbitMQTest extends ClusterTest {

    public void testMQ() throws Exception {
        testMQReq();
        testMQTopic();
    }
}
