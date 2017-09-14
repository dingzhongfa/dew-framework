package com.tairanchina.csp.dew.core.test;

import com.tairanchina.csp.dew.core.Dew;
import com.tairanchina.csp.dew.core.test.cluster.ClusterTest;
import com.tairanchina.csp.dew.core.test.crud.CRUDSTest;
import com.tairanchina.csp.dew.core.test.jdbc.JDBCTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class,webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ComponentScan(basePackageClasses = {Dew.class, TestAll.class})
public class TestAll {

    @Resource
    private ClusterTest clusterTest;

    @Resource
    private JDBCTest jdbcTest;

    @Resource
    private CRUDSTest crudsTest;

    @Test
    public void testCluster() throws Exception{
        clusterTest.testAll();
    }

    @Test
    public void testMQ() throws Exception{
        clusterTest.testMQ();
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
