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

    @GetMapping("/start")
    @ApiOperation(value = "示例方式")
    public String example() throws InterruptedException, IOException {
        int sleep = random.nextInt(100);
        TimeUnit.MILLISECONDS.sleep(sleep);
        Request request = new Request.Builder().url("http://localhost:8892/foo").get().build();
        Response response = client.newCall(request).execute();
        return " [service1 sleep " + sleep + " ms]" + response.body().toString();
    }

    @GetMapping("/second")
    @ApiOperation(value = "示例方式")
    public String second() throws InterruptedException, IOException {
        int sleep = random.nextInt(100);
        TimeUnit.MILLISECONDS.sleep(sleep);
        return " [service1 sleep " + sleep + " ms]";
    }
}
