package com.tairanchina.csp.dew.idempotent;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * desription:
 * Created by ding on 2017/11/22.
 */
@RestController
@RequestMapping("/idempotent/")
public class IdempotentController {

    @GetMapping(value = "test1", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> testIdem1(@RequestParam("str") String str) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(str);
    }

    @GetMapping(value = "test2", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> testIdem2(@RequestParam("str") String str) {
        DewIdempotent.confirm();
        return ResponseEntity.ok(str);
    }
}
