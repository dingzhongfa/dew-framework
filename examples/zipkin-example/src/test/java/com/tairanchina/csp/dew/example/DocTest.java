package com.tairanchina.csp.dew.example;

import com.tairanchina.csp.dew.core.Dew;
import com.tairanchina.csp.dew.example.zipkin.ZipkinExampleApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootApplication
@SpringBootTest(classes = {Dew.class, ZipkinExampleApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class DocTest {

    @Test
    public void empty(){}

}