package com.tairanchina.csp.dew.idempotent;

import com.ecfront.dew.common.$;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.HashMap;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = IdempotentApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class IdempotentTest {

    private static final Logger logger = LoggerFactory.getLogger(IdempotentTest.class);

    private String urlPre = "http://localhost:8080/idempotent/";

    @Test
    public void testUnconfirm() throws IOException {
        HashMap<String, String> hashMap = new HashMap<String, String>() {{
            put("__IDEMPOTENT_OPT_TYPE__", "dew-type");
            put("__IDEMPOTENT_OPT_ID__", "dew-id");
            put("__IDEMPOTENT_EXPIRE_MS__", "5000");
        }};
        String result1 = $.http.get(urlPre + "test1?str=dew-test", hashMap);
        String result2 = $.http.get(urlPre + "test1?str=dew-test", hashMap);
        hashMap.put("__IDEMPOTENT_FORCE__", "true");
        String result3 = $.http.get(urlPre + "test1?str=dew-test", hashMap);
        Assert.assertEquals("dew-test", result1);
        Assert.assertEquals("dew-test", result3);
        logger.info("result1:  " + result1);
        logger.info("result2:  " + result2);
        logger.info("result3:  " + result3);
    }

    @Test
    public void testConfirmed() throws IOException, InterruptedException {
        HashMap<String, String> hashMap = new HashMap<String, String>() {{
            put("__IDEMPOTENT_OPT_TYPE__", "dew-type");
            put("__IDEMPOTENT_OPT_ID__", "dew-id");
            put("__IDEMPOTENT_EXPIRE_MS__", "5000");
            put("__IDEMPOTENT_CONFIRM__", "false");
        }};
        String result1 = $.http.get(urlPre + "test1?str=dew-test", hashMap);
        Thread.sleep(2000);
        String result2 = $.http.get(urlPre + "test1?str=dew-test", hashMap);
        Thread.sleep(4000);
        String result3 = $.http.get(urlPre + "test1?str=dew-test", hashMap);
        Assert.assertEquals("dew-test", result1);
        Assert.assertEquals("dew-test", result3);
        logger.info("result1:  " + result1);
        logger.info("result2:  " + result2);
        logger.info("result3:  " + result3);
    }

    @Test
    public void testNeedConfirm() throws IOException, InterruptedException {
        HashMap<String, String> hashMap = new HashMap<String, String>() {{
            put("__IDEMPOTENT_OPT_TYPE__", "dew-type");
            put("__IDEMPOTENT_OPT_ID__", "dew-id");
            put("__IDEMPOTENT_EXPIRE_MS__", "5000");
        }};
        String result1 = $.http.get(urlPre + "test2?str=dew-test", hashMap);
        String result2 = $.http.get(urlPre + "test2?str=dew-test", hashMap);
        Assert.assertEquals("dew-test", result1);
        logger.info("result1:  " + result1);
        logger.info("result2:  " + result2);
    }

}
