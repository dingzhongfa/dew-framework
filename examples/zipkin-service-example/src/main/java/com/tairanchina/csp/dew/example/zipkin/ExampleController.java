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
    private OkHttpClient client;

    private Random random = new Random();
    
    @GetMapping("/foo")
    @ApiOperation(value = "示例方式")
    public String example() throws InterruptedException, IOException {
        Random random = new Random();
        int sleep= random.nextInt(10000);
        TimeUnit.MILLISECONDS.sleep(sleep);
        Request request = new Request.Builder().url("http://localhost:9091/bar").get().build();  //service3
        Response response = client.newCall(request).execute();
        String result = response.body().string();
        request = new Request.Builder().url("http://localhost:9092/tar").get().build();  //service4
        response = client.newCall(request).execute();
        result += response.body().string();
//        Request request = new Request.Builder().url("http://www.baidu.com").get().build();  //service3
//        Response response = client.newCall(request).execute();
        return " [service2 sleep " +" ms]"+result;
    }

}
