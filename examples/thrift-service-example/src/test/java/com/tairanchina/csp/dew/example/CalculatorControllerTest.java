package com.tairanchina.csp.dew.example;

import com.tairanchina.csp.dew.core.Dew;
import com.tairanchina.csp.dew.example.thrift.TCalculatorService;
import com.tairanchina.csp.dew.example.thrift.TOperation;
import com.tairanchina.csp.dew.example.thrift.ThriftApp;
import com.tairanchina.csp.dew.example.thrift.controller.HCalculatorController;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.transport.THttpClient;
import org.apache.thrift.transport.TTransport;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by 迹_Jason on 2017/08/09.
 */
@RunWith(SpringRunner.class)
@SpringBootApplication
@SpringBootTest(classes = {Dew.class, ThriftApp.class}, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CalculatorControllerTest {

    @Autowired
    TProtocolFactory protocolFactory;

    @Autowired
    HCalculatorController hCalculatorController;

    TCalculatorService.Iface client;

    @Before
    public void setUp() throws Exception {
        TTransport transport = new THttpClient("http://localhost:8890/thriftcaclcu");
        TProtocol protocol = protocolFactory.getProtocol(transport);
        client = new TCalculatorService.Client(protocol);
    }

    @Test
    public void testThriftCall() throws Exception {
        assertEquals(4, client.calculate(1, 3, TOperation.ADD));
        assertEquals(5, client.calculate(10, 5, TOperation.SUBTRACT));
    }

    @Test
    public void testHttpCall() throws Exception {
        Map<String, Object> caclculocal = hCalculatorController.caclculocal(1, 3, "1");
        assertEquals(4, caclculocal.get("1"));
    }
}