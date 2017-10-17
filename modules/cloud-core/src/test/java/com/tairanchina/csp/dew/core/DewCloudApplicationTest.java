package com.tairanchina.csp.dew.core;

import com.tairanchina.csp.dew.Dew;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import com.ecfront.dew.common.HttpHelper;

/**
 * DewCloudApplicationTest
 *
 * @author hzzjb
 * @date 2017/9/21
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DewCloudApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ComponentScan(basePackageClasses = {Dew.class, DewCloudApplication.class})
public class DewCloudApplicationTest {

    @Test
    public void test() throws Exception {
        HttpHelper.ResponseWrap serviceResponseWrap = Dew.EB.get("http://performance-service/performance");
        Assert.assertEquals(0,serviceResponseWrap.statusCode);
    }
}