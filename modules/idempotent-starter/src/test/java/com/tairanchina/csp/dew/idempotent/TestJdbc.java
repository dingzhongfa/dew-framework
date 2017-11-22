package com.tairanchina.csp.dew.idempotent;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = IdempotentApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TestJdbc {

}
