package com.tairanchina.csp.dew.example.thrift;

import com.tairanchina.csp.dew.core.Dew;
import com.tairanchina.csp.dew.example.thrift.client.CalculatorClientController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootApplication
@SpringBootTest(classes = ThriftExampleApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ComponentScan(basePackageClasses = {Dew.class, ThriftExampleApplication.class})
public class CalculatorServiceControllerTest {

    @Autowired
    CalculatorClientController calculatorClientController;

    @Test
    public void testThriftCall() throws Exception {
        Map<String, Object> caclcu = calculatorClientController.caclcu(1, 3, "1");
        assertEquals(4, caclcu.get("1"));
    }

    @Test
    public void testHttpCall() throws Exception {
        Map<String, Object> caclculocal = calculatorClientController.caclculocal(1, 3, "1");
        assertEquals(4, caclculocal.get("1"));
    }
}