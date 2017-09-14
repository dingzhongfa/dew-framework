package com.tairanchina.csp.dew.core.test;

import com.tairanchina.csp.dew.core.Dew;
import com.tairanchina.csp.dew.core.test.cluster.RabbitMQTest;
import com.tairanchina.csp.dew.core.test.cluster.RedisTest;
import com.tairanchina.csp.dew.core.test.crud.CRUDSTest;
import com.tairanchina.csp.dew.core.test.dataaccess.jdbc.JDBCTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class,webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ComponentScan(basePackageClasses = {Dew.class, TestAll.class})
public class TestAll {

    @Resource
    private RedisTest redisTest;

    @Resource
    private JDBCTest jdbcTest;

    @Resource
    private CRUDSTest crudsTest;

    @Resource
    private RabbitMQTest rabbitMQTest;

    @Test
    public void testCluster() throws Exception{
        redisTest.testAll();
    }

    @Test
    public void testMQ() throws Exception{
        redisTest.testMQ();

    }

    @Test
    public void testJDBC() throws Exception{
        jdbcTest.testAll();
        jdbcTest.testPoolA();
        jdbcTest.testPool();
    }

    @Test
    public void testCRUD() throws Exception{
        crudsTest.testAll();
    }

}
