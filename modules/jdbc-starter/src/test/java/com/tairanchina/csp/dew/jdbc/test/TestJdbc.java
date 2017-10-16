package com.tairanchina.csp.dew.jdbc.test;

import com.tairanchina.csp.dew.jdbc.test.crud.CRUDSTest;
import com.tairanchina.csp.dew.jdbc.test.ds.JDBCTest;
import com.tairanchina.csp.dew.jdbc.test.select.SelectTest;
import com.tairanchina.csp.dew.jdbc.sharding.ShardingTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = JDBCApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TestJdbc {
    @Resource
    private JDBCTest jdbcTest;

    @Resource
    private CRUDSTest crudsTest;

    @Resource
    private SelectTest selectTest;

    @Resource
    private ShardingTest shardingTest;

    /**
     * @throws Exception
     */
    @Test
    public void testJDBC() throws Exception {
        jdbcTest.testAll();
    }


    /**
     * Select注解测试
     *
     * @throws Exception
     */
    @Test
    public void testSelect() throws Exception {
        selectTest.testAll();
    }

    /**
     * 脚手架测试
     *
     * @throws Exception
     */
    @Test
    public void testCRUD() throws Exception {
        crudsTest.testAll();
    }

    @Test
    public void testShardingJDBC()throws Exception{
        shardingTest.testSharding();
    }
}
