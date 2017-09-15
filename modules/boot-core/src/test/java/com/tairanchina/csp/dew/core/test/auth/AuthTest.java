package com.tairanchina.csp.dew.core.test.auth;


import com.ecfront.dew.common.$;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class AuthTest {

    private String URL = "http://127.0.0.1:8080/";

    private Logger logger = LoggerFactory.getLogger(AuthTest.class);

    public void testAuth() throws Exception {
        AuthExampleController.User user = new AuthExampleController.User();
        user.setIdCard("331023395739483150");
        user.setName("辰");
        user.setPassword("123456");
        user.setPhone("15957199704");
        String registerRes = $.http.post(URL + "user/register", user);
        Assert.assertEquals("200", $.json.toJson(registerRes).get("code").asText());
        AuthExampleController.LoginDTO loginDTO = new AuthExampleController.LoginDTO();
        loginDTO.setIdCard(user.getIdCard());
        loginDTO.setPassword(user.getPassword());
        String loginRes = $.http.post(URL + "auth/login", loginDTO);
        String token = $.json.toJson(loginRes).get("body").asText();
        logger.info(loginRes);
        Assert.assertEquals("200", $.json.toJson(loginRes).get("code").asText());
        String businRes = $.http.get(URL + "business/someopt", new HashMap<String, String>() {{
                put("_token_", token);
        }});
        logger.info(businRes);
        Assert.assertEquals("200", $.json.toJson(businRes).get("code").asText());
        String logoutRes = $.http.delete(URL + "auth/logout",new HashMap<String ,String>(){{
            put("_token_", token);
        }});
        Assert.assertEquals("200", $.json.toJson(logoutRes).get("code").asText());
    }
}
