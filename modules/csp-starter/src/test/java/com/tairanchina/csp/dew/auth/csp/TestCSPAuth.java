package com.tairanchina.csp.dew.auth.csp;

import com.ecfront.dew.common.$;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.HashMap;


/**
 * desription:
 * Created by ding on 2017/10/30.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CSPApplication.class,webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TestCSPAuth {

    @Test
    public void testCSOAuth() throws IOException {
        String result = $.http.get("http://localhost:8080/csp-auth/business/someopt",new HashMap(){{
            put("_token_","token_test");
            put("X-User-Id","user_id_test");
            put("X-App-Id","app_id_test");
            put("X-Roles","USER,MANAGER");
        }});
        Assert.assertNotNull(result);
    }
}
