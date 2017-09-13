package com.tairanchina.csp.dew.core.test.select;

import com.ecfront.dew.common.Page;
import com.tairanchina.csp.dew.core.Dew;
import com.tairanchina.csp.dew.core.DewBootApplication;
import com.tairanchina.csp.dew.core.test.select.dao.SystemConfigDao;
import com.tairanchina.csp.dew.core.test.select.dao.TestInterfaceDao;
import com.tairanchina.csp.dew.core.test.select.entity.SystemConfig;
import com.tairanchina.csp.dew.core.test.select.entity.TestSelectEntity;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootApplication
@SpringBootTest(classes = DewBootApplication.class)
@ComponentScan(basePackageClasses = {Dew.class, JDBCAnnotationsTest.class})
public class JDBCAnnotationsTest {

    @Autowired
    private TestInterfaceDao dao;

    @Autowired
    private SystemConfigDao systemConfigDao;

    @Before
    public void initialize() throws Exception {
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
        Dew.ds().jdbc().execute("insert INTO test_select_entity(code,field_a,field_c,create_user,create_time,update_user,update_time,enabled) " +
                "values('aa','测试A','测试B','jiaj','2017-07-08','j','2017-07-08',TRUE)");

        Dew.ds().jdbc().execute("CREATE TABLE `system_config` (\n" +
                "  `id` char(32) NOT NULL,\n" +
                "  `value` varchar(200) DEFAULT NULL COMMENT '参数数值',\n" +
                "  `description` varchar(255) DEFAULT NULL COMMENT '参数说明',\n" +
                "  `level` varchar(255) DEFAULT NULL COMMENT '级别（字典）',\n" +
                "  `create_user` char(32) DEFAULT NULL COMMENT '创建人编码',\n" +
                "  `create_time` datetime DEFAULT NULL COMMENT '创建时间',\n" +
                "  `update_user` char(32) DEFAULT NULL COMMENT '更新人编码',\n" +
                "  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',\n" +
                "  PRIMARY KEY (`id`)\n" +
                ") COMMENT='系统配置';");
        Dew.ds().jdbc().execute("INSERT INTO system_config (`id`,`value`,`description`,`level`,create_user,create_time,update_user,update_time) " +
                "VALUES ('id','value','description','level','jiaj','2017-07-08','j','2017-07-08')");
    }

    @Test
    public void testInterface() throws Exception {
        TestSelectEntity model = new TestSelectEntity();
        List<TestSelectEntity> testSelectEntities = dao.findAll();
        List<SystemConfig> systemConfigs = systemConfigDao.findAll();
        Assert.assertNotNull(testSelectEntities);
        model.setFieldA("测试A");
        Page<TestSelectEntity> page = dao.queryByCustomPaging(model, 1L, 10);
        Assert.assertTrue(page != null);
        page = dao.queryByDefaultPaging(model);
        Assert.assertTrue(page != null);
        List<TestSelectEntity> list = dao.queryList(model);
        Assert.assertTrue(list != null);
        list = dao.queryByField("测试A");
        Assert.assertTrue(list != null);
        list = dao.queryByTowFields("测试A", "测试B");
        Assert.assertTrue(list != null);
        Map<String, Object> objectMap = dao.getMapById(1);
        Assert.assertTrue(objectMap != null);
        model = dao.getById(1);
        Assert.assertTrue(model != null);
    }

    @Test
    public void testLink() {
        List<TestSelectEntity> testSelectEntities = dao.findAll();
        List<SystemConfig> systemConfigs = systemConfigDao.findAll();
        TestSelectEntity model = new TestSelectEntity();
        Page<TestSelectEntity> page = dao.queryByCustomPaging(model, 1L, 10);
        Assert.assertTrue(page != null);
        Map map = systemConfigDao.testLink("value");
        Assert.assertNotNull(map);
    }
}
