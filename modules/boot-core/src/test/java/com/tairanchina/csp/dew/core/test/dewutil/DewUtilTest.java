package com.tairanchina.csp.dew.core.test.dewutil;


import com.ecfront.dew.common.$;
import com.ecfront.dew.common.HttpHelper;
import com.tairanchina.csp.dew.core.Container;
import com.tairanchina.csp.dew.core.Dew;
import com.tairanchina.csp.dew.core.test.TestAll;
import com.tairanchina.csp.dew.core.test.dewutil.controller.UtilController;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class DewUtilTest {

    private Logger logger = LoggerFactory.getLogger(DewUtilTest.class);

    private final String URL = TestAll.URL+"consumer/";

    public void testAll() throws Exception {
        testDewRest();
        testTimer();
    }

    private void testDewRest() throws Exception {
        UtilController.Consumer consumer = new UtilController.Consumer();
        consumer.setId($.field.createUUID());
        consumer.setName("张三");
        consumer.setPassword("123456");
        logger.info("----test----");
        HttpHelper.ResponseWrap responseWrap = Dew.EB.head(URL + "save");
        Assert.assertEquals(200,responseWrap.statusCode);
        HttpHelper.ResponseWrap saveResponseWrap = Dew.EB.post(URL + "save", consumer);
        Assert.assertEquals(200,saveResponseWrap.statusCode);
        HttpHelper.ResponseWrap getResponseWrap = Dew.EB.get(URL + "get?id="+consumer.getId());
        Assert.assertEquals(200,getResponseWrap.statusCode);
        HttpHelper.ResponseWrap updateResponseWrap = Dew.EB.put(URL + "update", consumer);
        Assert.assertEquals(200,updateResponseWrap.statusCode);
        HttpHelper.ResponseWrap deleteResponseWrap = Dew.EB.delete(URL + "delete?id="+consumer.getId());
        Assert.assertEquals(200,deleteResponseWrap.statusCode);
        logger.info("----test----");
    }


    private void testTimer()throws InterruptedException{
        long startTime = System.currentTimeMillis();
        Dew.Timer.timer(1,()->logger.info("fun:    "+String.valueOf(System.currentTimeMillis() - startTime)));
        logger.info("end:   "+String.valueOf(System.currentTimeMillis() - startTime));
        Thread.sleep(1500);
        Container container = new Container();
    }
}
