package com.tairanchina.csp.dew.example.rest;

import com.ecfront.dew.common.HttpHelper;
import com.tairanchina.csp.dew.core.Dew;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@RestController
@Api(description = "示例应用")
public class ExampleController {
    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/start")
    @ApiOperation(value = "示例方式")
    public String example() {
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("request-from", "Rest-Service");
//        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
//        ResponseEntity<String> result = restTemplate.exchange("http://SERVICE-CLOUD-EXAMPLE/example", HttpMethod.GET, entity, String.class);
        HttpHelper.WrapHead result = Dew.EB.get("http://SERVICE-CLOUD-EXAMPLE/example");
        return result.result;
    }

}
