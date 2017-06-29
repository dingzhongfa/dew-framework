package com.tairanchina.csp.dew.core;

import org.junit.Assert;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by panshuai on 17/6/29.
 */
@Component
public class TxService {

    @Transactional
    public void testCommit(){
        JDBCTest.BasicEntity basicEntity = new JDBCTest.BasicEntity();
        basicEntity.setFieldA("TransactionA1");
        basicEntity.setFieldB("TransactionA1");
        long id = Dew.ds().insert(basicEntity);
        Assert.assertTrue(id != 0);
    }

    @Transactional(rollbackFor = {Exception.class,RuntimeException.class,NullPointerException.class})
    public void testRollBack(){
        JDBCTest.BasicEntity basicEntity = new JDBCTest.BasicEntity();
        basicEntity.setFieldA("TransactionA2");
        basicEntity.setFieldB("TransactionA2");
        long id = Dew.ds().insert(basicEntity);
        Assert.assertTrue(id != 0);
        String string  = null;
        if(string.equals("")) {
            int i = 0;
        }
    }

    @Transactional("test2TransactionManager")
    public void testMultiCommit(){
        JDBCTest.BasicEntity basicEntity = new JDBCTest.BasicEntity();
        basicEntity.setFieldA("TransactionA1");
        basicEntity.setFieldB("TransactionA1");
        long id = Dew.ds("test2").insert(basicEntity);
        Assert.assertTrue(id != 0);
    }

    @Transactional(value = ("test2TransactionManager"), rollbackFor = {Exception.class,RuntimeException.class,NullPointerException.class})
    public void testMultiRollBack(){
        JDBCTest.BasicEntity basicEntity = new JDBCTest.BasicEntity();
        basicEntity.setFieldA("TransactionA2");
        basicEntity.setFieldB("TransactionA2");
        long id = Dew.ds("test2").insert(basicEntity);
        Assert.assertTrue(id != 0);
        String string  = null;
        if(string.equals("")) {
            int i = 0;
        }
    }


}
