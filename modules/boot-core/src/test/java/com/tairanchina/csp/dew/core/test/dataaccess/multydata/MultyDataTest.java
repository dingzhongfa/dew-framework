package com.tairanchina.csp.dew.core.test.dataaccess.multydata;


import com.tairanchina.csp.dew.core.Dew;
import org.junit.Assert;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class MultyDataTest {

    public void testAll(){
        init();
        testPool();
        testPoolA();
    }

    private void init(){
        Dew.ds().jdbc().execute("DROP TABLE if EXISTS test_select_entity");
        Dew.ds().jdbc().execute("CREATE TABLE IF NOT EXISTS test_select_entity\n" +
                "(\n" +
                "id int primary key auto_increment,\n" +
                "code varchar(32),\n" +
                "field_a varchar(255),\n" +
                "field_c varchar(255) not null,\n" +
                "create_user varchar(32) not null,\n" +
                "create_time datetime,\n" +
                "update_user varchar(32) not null,\n" +
                "update_time datetime,\n" +
                "enabled bool\n" +
                ")");
        Dew.ds().jdbc().execute("INSERT  INTO  test_select_entity " +
                "(code,field_a,field_c,create_user,create_time,update_user,update_time,enabled) VALUES " +
                "('A','A-a','A-b','ding',NOW(),'ding',NOW(),TRUE )");

        Dew.ds("test2").jdbc().execute("DROP TABLE if EXISTS test_select_entity");
        Dew.ds("test2").jdbc().execute("CREATE TABLE IF NOT EXISTS test_select_entity\n" +
                "(\n" +
                "id int primary key auto_increment,\n" +
                "code varchar(32),\n" +
                "field_a varchar(255),\n" +
                "field_c varchar(255) not null,\n" +
                "create_user varchar(32) not null,\n" +
                "create_time datetime,\n" +
                "update_user varchar(32) not null,\n" +
                "update_time datetime,\n" +
                "enabled bool\n" +
                ")");
        Dew.ds("test2").jdbc().execute("INSERT  INTO  test_select_entity " +
                "(code,field_a,field_c,create_user,create_time,update_user,update_time,enabled) VALUES " +
                "('A','A-a','A-b','ding',NOW(),'ding',NOW(),TRUE )");
    }

    @Transactional
    private void testPool() {
        Boolean[] hasFinish={false};
        Dew.ds().jdbc().queryForList("select * from test_select_entity").size();
        new Thread(() -> {
            Dew.ds().jdbc().queryForList("select * from test_select_entity").size();
            Assert.assertTrue(hasFinish[0]);
        }).start();
        try {
            Thread.sleep(1);
            hasFinish[0]=true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Transactional("test2TransactionManager")
    private void testPoolA() {
        Boolean[] hasFinish={false};
        Dew.ds("test2").jdbc().queryForList("select * from test_select_entity").size();
        new Thread(() -> {
            Dew.ds("test2").jdbc().queryForList("select * from test_select_entity").size();
            Assert.assertTrue(hasFinish[0]);
        }).start();
        try {
            Thread.sleep(1);
            hasFinish[0]=true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
