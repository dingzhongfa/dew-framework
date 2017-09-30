package com.tairanchina.csp.dew.example;

import com.tairanchina.csp.dew.core.Dew;
import com.tairanchina.csp.dew.example.web.WebExampleApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootApplication
@SpringBootTest(classes = WebExampleApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ComponentScan(basePackageClasses = {Dew.class, WebExampleApplication.class})
public class DocTest {

    @Test
    public void empty() {
    }

}