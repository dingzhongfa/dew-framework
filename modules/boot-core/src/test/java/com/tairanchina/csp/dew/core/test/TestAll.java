package com.tairanchina.csp.dew.core.test;

import com.tairanchina.csp.dew.core.Dew;
import com.tairanchina.csp.dew.core.test.auth.AuthTest;
import com.tairanchina.csp.dew.core.test.cluster.ClusterTest;
import com.tairanchina.csp.dew.core.test.crud.CRUDSTest;
import com.tairanchina.csp.dew.core.test.dataaccess.jdbc.JDBCTest;
import com.tairanchina.csp.dew.core.test.dataaccess.multydata.MultyDataTest;
import com.tairanchina.csp.dew.core.test.dataaccess.select.SelectTest;
import com.tairanchina.csp.dew.core.test.dewutil.DewUtilTest;
import com.tairanchina.csp.dew.core.test.web.WebTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ComponentScan(basePackageClasses = {Dew.class, TestAll.class})
public class TestAll {

    @Resource
    private ClusterTest clusterTest;

    @Resource
    private JDBCTest jdbcTest;

    @Resource
    private CRUDSTest crudsTest;

    @Resource
    private SelectTest selectTest;

    @Resource
    private WebTest webTest;

    @Resource
    private MultyDataTest multyDataTest;

    @Resource
    private AuthTest authTest;

    @Resource
    private DewUtilTest dewUtilTest;

    /**
     * 缓存，map缓存，分布式锁测试
     * @throws Exception
     */
    @Test
    public void testCluster() throws Exception {
        clusterTest.testAll();
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void testJDBC() throws Exception {
        jdbcTest.testAll();
    }

    /**
     * 多数据源测试
     */
    @Test
    public void testMultyData() {
        multyDataTest.testAll();
    }

    /**
     * Select注解测试
     * @throws Exception
     */
    @Test
    public void testSelect() throws Exception {
        selectTest.testAll();
    }

    /**
     * 脚手架测试
     * @throws Exception
     */
    @Test
    public void testCRUD() throws Exception {
        crudsTest.testAll();
    }

    /**
     * 数据验证测、响应格式、异常处理等测试
     * @throws Exception
     */
    @Test
    public void testWeb() throws Exception {
        webTest.testAll();
    }

    /**
     * 权限测试，含logger测试
     * @throws Exception
     */
    @Test
    public void testAuth() throws Exception {
        authTest.testAuth();
    }

    /**
     * Dew类的util测试
     * @throws Exception
     */
    @Test
    public void testDewUtil() throws Exception{
        dewUtilTest.testAll();
    }
}
