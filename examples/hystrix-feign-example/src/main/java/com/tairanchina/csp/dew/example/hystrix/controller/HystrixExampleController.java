package com.tairanchina.csp.dew.example.hystrix.controller;

import com.tairanchina.csp.dew.example.hystrix.client.ExampleClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;


@RestController
@RequestMapping("example/")
public class HystrixExampleController {

    private static final Logger logger = LoggerFactory.getLogger(HystrixExampleController.class);

    public static int s=0;

    @Autowired
    private ExampleClient exampleClient;

    @GetMapping("get-exe/ori")
    public ResponseEntity getExe() throws InterruptedException {
        logger.info("post-exe   " );
        for (int m =0;m<80000;m++){
            exampleClient.postExe(m,"post");
            Thread.sleep(100);
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("get-exe")
    public ResponseEntity getExe(@RequestParam("i") int i,@RequestParam("str") String str) throws InterruptedException {
        logger.info("get-exe   " + "i=" + i + "str=" + str);
        for (int m =0;m<80000;m++){
            exampleClient.deleteExe(m,"delete");
            Thread.sleep(100);
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("post-exe")
    public ResponseEntity postExe(@RequestParam("i") int i, @RequestParam("str") String str) {
        logger.info("post-exe   " + "i=" + i + "str=" + str);
        if (i>600){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(404).build();
    }

    @PutMapping("put-exe")
    public ResponseEntity putExe(@RequestParam("i") int i, @RequestParam("str") String str) {
        logger.info("put-exe   " + "i=" + i + "str=" + str);
        return ResponseEntity.ok().build();
    }
}
