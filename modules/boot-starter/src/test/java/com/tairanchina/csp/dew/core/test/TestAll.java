package com.tairanchina.csp.dew.core.test;

import com.tairanchina.csp.dew.core.test.auth.AuthTest;
import com.tairanchina.csp.dew.core.test.cluster.ClusterTest;
import com.tairanchina.csp.dew.core.test.dewutil.DewUtilTest;
import com.tairanchina.csp.dew.core.test.web.WebTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = BootTestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TestAll {

    public static final String URL = "http://127.0.0.1:8080/";

    @Resource
    private ClusterTest clusterTest;

    @Resource
    private WebTest webTest;

    @Resource
    private AuthTest authTest;

    @Resource
    private DewUtilTest dewUtilTest;


    /**
     * 缓存，map缓存，分布式锁测试
     *
     * @throws Exception
     */
    @Test
    public void testCluster() throws Exception {
        clusterTest.testAll();
    }

    /**
     * 数据验证测、响应格式、异常处理等测试
     *
     * @throws Exception
     */
    @Test
    public void testWeb() throws Exception {
        webTest.testAll();
    }

    /**
     * 权限测试，含logger测试
     *
     * @throws Exception
     */
    @Test
    public void testAuth() throws Exception {
        authTest.testAuth();
    }

    /**
     * Dew类的util测试
     *
     * @throws Exception
     */
    @Test
    public void testDewUtil() throws Exception {
        dewUtilTest.testAll();
    }


}
