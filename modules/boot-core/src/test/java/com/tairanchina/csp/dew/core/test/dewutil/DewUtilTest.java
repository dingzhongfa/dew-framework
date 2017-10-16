package com.tairanchina.csp.dew.core.test.dewutil;


import com.ecfront.dew.common.$;
import com.ecfront.dew.common.HttpHelper;
import com.tairanchina.csp.dew.core.Container;
import com.tairanchina.csp.dew.Dew;
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
        testTimer();
    }



    private void testTimer()throws InterruptedException{
        long startTime = System.currentTimeMillis();
        Dew.Timer.timer(1,()->logger.info("fun:    "+String.valueOf(System.currentTimeMillis() - startTime)));
        logger.info("end:   "+String.valueOf(System.currentTimeMillis() - startTime));
        Thread.sleep(1500);
        Container container = new Container();
    }
}
