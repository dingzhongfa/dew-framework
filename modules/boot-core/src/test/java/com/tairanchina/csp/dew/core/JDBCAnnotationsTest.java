package com.tairanchina.csp.dew.core;

import com.tairanchina.csp.dew.core.dao.TestInterfaceDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootApplication
@SpringBootTest(classes = DewBootApplication.class)
@ComponentScan(basePackageClasses = {Dew.class, JDBCAnnotationsTest.class})
public class JDBCAnnotationsTest {

    @Autowired
    TestInterfaceDao dao;

    @Before
    private void initialize() throws Exception {
        Dew.ds().jdbc().execute("CREATE TABLE IF NOT EXISTS t_test_crud_s_entity\n" +
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
        Dew.ds().jdbc().execute("insert INTO t_test_crud_s_entity(code,field_a,field_c,create_user,create_time,update_user,update_time,enabled) " +
                "values('aa','测试A','测试B','jiaj','2017-07-08','j','2017-07-08',TRUE)");
    }

    @Test
    public void testInterface() throws Exception {
        CRUDSTestEntity model = new CRUDSTestEntity();
        model.setFieldA("测试A");
        // Page<CRUDSTestEntity> page = dao.test1(model, 1L, 10);
        // Assert.assertTrue(page != null);
    }
}
