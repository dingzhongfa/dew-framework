package com.tairanchina.csp.dew.core.test.cluster;


import org.springframework.stereotype.Component;


@Component
public class RedisTest extends ClusterTest {

    public void testAll() throws Exception {
        testCache();
        testDistMapExp();
        testDistLock();
        testDistLockWithFun();
        testSameThreadLock();
        testDiffentTreadLock();
        testUnLock();
        testWaitLock();
        testConnection();
        testDistMap();
    }

    public void testMQ() throws Exception {
        testMQReq();
        testMQTopic();
    }


}

