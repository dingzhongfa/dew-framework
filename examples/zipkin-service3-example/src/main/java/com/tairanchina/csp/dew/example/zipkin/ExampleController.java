package com.tairanchina.csp.dew.example.zipkin;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@RestController
@Api(description = "示例应用")
public class ExampleController {
    @Autowired
    OkHttpClient client;

    @GetMapping("/bar")
    @ApiOperation(value = "示例方式")
    public String example() throws Exception {
        Random random = new Random();
        int sleep = random.nextInt(1000);
        TimeUnit.MILLISECONDS.sleep(sleep);
        Request request = new Request.Builder().url("http://localhost:8891/second").get().build();  //service4
        Response response = client.newCall(request).execute();
        String result = response.body().string();
        return " [service4 sleep " + sleep + " ms]" + result;
    }

}
