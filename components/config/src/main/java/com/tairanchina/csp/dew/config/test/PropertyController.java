package com.tairanchina.csp.dew.config.test;

import com.tairanchina.csp.dew.config.DewConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.config.server.ssh.SshUriProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class PropertyController {

    @Value("${ding.test:唉}")
    private String test;

    @Value(("${spring.cloud.client.hostname}"))
    private String hostname;

    @Autowired
    private DewConfig dewConfig;

    @GetMapping("ding/test")
    public ResponseEntity<String> getTest(){
        return ResponseEntity.ok(test);
    }

    @GetMapping("ding/dew")
    public ResponseEntity<DewConfig> getHostname(){
        return ResponseEntity.ok(dewConfig);
    }



}
