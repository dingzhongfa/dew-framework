package com.tairanchina.csp.dew.core.test.jdbc;

import com.ecfront.dew.common.Page;
import com.tairanchina.csp.dew.core.*;
import com.tairanchina.csp.dew.core.test.jdbc.entity.BasicEntity;
import com.tairanchina.csp.dew.core.test.jdbc.entity.EmptyEntity;
import com.tairanchina.csp.dew.core.test.jdbc.entity.FullEntity;
import com.tairanchina.csp.dew.core.test.jdbc.util.TxService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@RunWith(SpringRunner.class)
@SpringBootApplication
@SpringBootTest(classes = DewBootApplication.class)
@ComponentScan(basePackageClasses = {Dew.class, JDBCTest.class})
public class JDBCTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private TxService txService;

    @Autowired
    @Qualifier("test2JdbcTemplate")
    private JdbcTemplate jdbcTemplate2;


    /**
     * 测试没有配置多数据库的情况
     * 在配置中注释掉multi-datasources:
     */
   /* @Test
    public void testNotDynamic(){
        int temp = Dew.ds().jdbc().queryForList("select * from basic_entity").size();
        Assert.assertTrue(temp == 1);
    }*/

   @Before
   public void init(){
       Dew.ds().jdbc().execute("DROP TABLE IF EXISTS `basic_entity`;");
       Dew.ds().jdbc().execute("CREATE TABLE basic_entity\n" +
               "(\n" +
               "id int primary key auto_increment,\n" +
               "field_a varchar(255)\n" +
               ")");
       Dew.ds().jdbc().execute("DROP TABLE IF EXISTS `full_entity`;");
       Dew.ds().jdbc().execute("CREATE TABLE full_entity\n" +
               "(\n" +
               "id int primary key auto_increment,\n" +
               "code varchar(255),\n" +
               "field_a varchar(255),\n" +
               "field_c varchar(255) not null,\n" +
               "create_user varchar(255) not null,\n" +
               "create_time datetime,\n" +
               "update_user varchar(255) not null,\n" +
               "update_time datetime,\n" +
               "enabled bool\n" +
               ")");
   }

    @Test
    public void testAll() throws Exception {
        testEntity();
        testMultiDS();
        testTx();
    }

    public void testEntity() throws InterruptedException {
        // 没有Entity注解的类，异常
        try {
            Dew.ds().findAll(EmptyEntity.class);
            Assert.assertTrue(false);
        } catch (Throwable e) {
            Assert.assertTrue(true);
        }
        // =========== Basic Test
        // findAll
        Assert.assertEquals(0, Dew.ds().findAll(BasicEntity.class).size());
        // insert
        BasicEntity basicEntity = new BasicEntity();
        basicEntity.setFieldA("测试A");
        basicEntity.setFieldB("测试B");
        Object id = Dew.ds().insert(basicEntity);
        // getById
        Assert.assertEquals("测试A", Dew.ds().getById(id, BasicEntity.class).getFieldA());
        // updateById
        basicEntity.setFieldA("测试C");
        Dew.ds().updateById(id, basicEntity);
        Assert.assertEquals("测试C", Dew.ds().getById(id, BasicEntity.class).getFieldA());
        // findAll
        Assert.assertEquals(1, Dew.ds().findAll(BasicEntity.class).size());
        try {
            Dew.ds().findEnabled(BasicEntity.class);
            Assert.assertTrue(false);
        } catch (Throwable e) {
            Assert.assertTrue(true);
        }
        // =========== Full Test
        FullEntity fullEntity = new FullEntity();
        fullEntity.setFieldA("测试A");
        // insert
        try {
            Dew.ds().insert(fullEntity);
            Assert.assertTrue(false);
        } catch (Throwable e) {
            Assert.assertTrue(true);
        }
        fullEntity.setFieldB("测试B");
        id = Dew.ds().insert(fullEntity);
        // getById
        fullEntity = Dew.ds().getById(id, FullEntity.class);
        Assert.assertTrue(!fullEntity.getCode().isEmpty());
        Assert.assertEquals("测试A", fullEntity.getFieldA());
        Assert.assertEquals("测试B", fullEntity.getFieldB());
        // getByCode
        fullEntity = Dew.ds().getByCode(fullEntity.getCode(), FullEntity.class);
        Assert.assertEquals("", fullEntity.getCreateUser());
        Assert.assertEquals("", fullEntity.getUpdateUser());
        Assert.assertTrue(fullEntity.getCreateTime() != null);
        Assert.assertEquals(fullEntity.getCreateTime(), fullEntity.getUpdateTime());
        // updateById
        fullEntity.setFieldA("测试C");
        Dew.ds().updateById(id, fullEntity);
        Assert.assertEquals("测试C", Dew.ds().getById(id, FullEntity.class).getFieldA());
        // updateByCode
        fullEntity.setFieldA(null);
        fullEntity.setFieldB("测试D");
        // null不更新
        Thread.sleep(1000);
        Dew.ds().updateByCode(fullEntity.getCode(), fullEntity);
        fullEntity = Dew.ds().getById(id, FullEntity.class);
        Assert.assertEquals("测试C", fullEntity.getFieldA());
        Assert.assertEquals("测试D", fullEntity.getFieldB());
        Assert.assertNotEquals(fullEntity.getCreateTime(), fullEntity.getUpdateTime());
        Assert.assertEquals(true, fullEntity.getEnabled());
        // disableById
        Dew.ds().disableById(fullEntity.getId(), FullEntity.class);
        Assert.assertEquals(false, Dew.ds().getById(fullEntity.getId(), FullEntity.class).getEnabled());
        // enableById
        Dew.ds().enableById(fullEntity.getId(), FullEntity.class);
        Assert.assertEquals(true, Dew.ds().getById(fullEntity.getId(), FullEntity.class).getEnabled());
        // disableByCode
        Dew.ds().disableByCode(fullEntity.getCode(), FullEntity.class);
        Assert.assertEquals(false, Dew.ds().getById(fullEntity.getId(), FullEntity.class).getEnabled());
        // enableByCode
        Dew.ds().enableByCode(fullEntity.getCode(), FullEntity.class);
        Assert.assertEquals(true, Dew.ds().getById(fullEntity.getId(), FullEntity.class).getEnabled());
        // existById
        Assert.assertEquals(true, Dew.ds().existById(fullEntity.getId(), FullEntity.class));
        Assert.assertEquals(false, Dew.ds().existById(11111, FullEntity.class));
        // existByCode
        Assert.assertEquals(true, Dew.ds().existByCode(fullEntity.getCode(), FullEntity.class));
        Assert.assertEquals(false, Dew.ds().existByCode("11111", FullEntity.class));
        // findAll
        Assert.assertEquals(1, Dew.ds().findAll(FullEntity.class).size());
        Assert.assertEquals("测试C", Dew.ds().findAll(FullEntity.class).get(0).getFieldA());
        // findEnabled
        Assert.assertEquals(1, Dew.ds().findEnabled(FullEntity.class).size());
        // findDisabled
        Assert.assertEquals(0, Dew.ds().findDisabled(FullEntity.class).size());
        // countAll
        Assert.assertEquals(1, Dew.ds().countAll(FullEntity.class));
        // countEnabled
        Assert.assertEquals(1, Dew.ds().countEnabled(FullEntity.class));
        // countDisabled
        Assert.assertEquals(0, Dew.ds().countDisabled(FullEntity.class));
        // insert
        FullEntity fullEntity2 = new FullEntity();
        fullEntity2.setFieldA("测试A2");
        fullEntity2.setFieldB("测试B2");
        FullEntity fullEntity3 = new FullEntity();
        fullEntity3.setFieldA("测试A3");
        fullEntity3.setFieldB("测试B3");
        Dew.ds().insert(new ArrayList<FullEntity>() {{
            add(fullEntity2);
            add(fullEntity3);
        }});
        Assert.assertEquals(3, Dew.ds().countAll(FullEntity.class));
        // paging
        Page<FullEntity> fullEntities = Dew.ds().paging(1, 2, FullEntity.class);
        Assert.assertEquals(3, fullEntities.getRecordTotal());
        Assert.assertEquals(2, fullEntities.getPageSize());
        Assert.assertEquals(1, fullEntities.getPageNumber());
        Assert.assertEquals(2, fullEntities.getPageTotal());
        Assert.assertEquals(2, fullEntities.getObjects().size());
        // pagingEnabled
        Dew.ds().disableById(fullEntity.getId(), FullEntity.class);
        fullEntities = Dew.ds().pagingEnabled(1, 2, FullEntity.class);
        Assert.assertEquals(2, fullEntities.getRecordTotal());
        // pagingDisabled
        fullEntities = Dew.ds().pagingDisabled(1, 2, FullEntity.class);
        Assert.assertEquals(1, fullEntities.getRecordTotal());
        // deleteById
        Dew.ds().deleteById(fullEntity.getId(), FullEntity.class);
        // deleteByCode
        Dew.ds().deleteByCode(Dew.ds().findAll(FullEntity.class).get(0).getCode(), FullEntity.class);
        Assert.assertEquals(1, Dew.ds().findAll(FullEntity.class).size());
    }

    public void testTx(){
        txService.testCommit();
        try {
            txService.testRollBack();
        } catch (Exception e){

        }
        txService.testMultiCommit();
        try {
            txService.testMultiRollBack();
        } catch (Exception e){

        }
        int res = Dew.ds().jdbc().queryForList("select * from basic_entity where field_a = 'TransactionA1'").size();
        Assert.assertTrue(res > 0);
        res = Dew.ds().jdbc().queryForList("select * from basic_entity where field_a = 'TransactionA2'").size();
        Assert.assertTrue(res == 0);
        res = Dew.ds("test2").jdbc().queryForList("select * from basic_entity where field_a = 'TransactionA1'").size();
        Assert.assertTrue(res > 0);
        res = Dew.ds("test2").jdbc().queryForList("select * from basic_entity where field_a = 'TransactionA2'").size();
        Assert.assertTrue(res == 0);
    }

    public void testMultiDS(){
        Dew.ds().jdbc().queryForList("select * from basic_entity").size();
        try {
            Dew.ds("test1").jdbc().queryForList("select * from basic_entity").size();
            Assert.assertFalse(1==1);
        }catch (Exception e){
            Assert.assertTrue(1==1);
        }
        Dew.ds("test2").jdbc().execute("CREATE TABLE basic_entity\n" +
                "(\n" +
                "id int primary key auto_increment,\n" +
                "field_a varchar(255)\n" +
                ")");
        Assert.assertEquals(0,Dew.ds("test2").jdbc().queryForList("select * from basic_entity").size());

        // 测试spring 直接注入jdbcTemplate的情况，是否生效
        int temp = jdbcTemplate.queryForList("select * from basic_entity").size();
        Assert.assertTrue(temp == 1);
        temp = jdbcTemplate2.queryForList("select * from basic_entity").size();
        Assert.assertTrue(temp == 0);
    }

    @Transactional
    @Test
    public void testPool() {
        Boolean[] hasFinish={false};
        Dew.ds().jdbc().queryForList("select * from basic_entity").size();
        new Thread(() -> {
            Dew.ds().jdbc().queryForList("select * from basic_entity").size();
            Assert.assertTrue(hasFinish[0]);
        }).start();
        try {
            Thread.sleep(2);
            hasFinish[0]=true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Transactional("test2TransactionManager")
    public void testPoolA() {
        Boolean[] hasFinish={false};
        Dew.ds("test2").jdbc().queryForList("select * from basic_entity").size();
        new Thread(() -> {
            Dew.ds("test2").jdbc().queryForList("select * from basic_entity").size();
            Assert.assertTrue(hasFinish[0]);
        }).start();
        try {
            Thread.sleep(2);
            hasFinish[0]=true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
