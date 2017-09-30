package com.tairanchina.csp.dew.example.thrift.service;

import com.tairanchina.csp.dew.example.thrift.TCalculatorService;
import com.tairanchina.csp.dew.example.thrift.TOperation;
import com.tairanchina.csp.dew.rpc.thrift.server.annotation.ThriftService;
import org.apache.thrift.TException;


@ThriftService(value = "/thriftcaclcu")
public class CalculatorServiceController implements TCalculatorService.Iface {

    @Override
    public int calculate(int num1, int num2, TOperation op) throws TException {
        int r = 0;
        switch (op) {
            case ADD:
                r = num1 + num2;
                break;
            case SUBTRACT:
                r = num1 - num2;
                break;
            case MULTIPLY:
                r = num1 * num2;
                break;
            case DIVIDE:
                r = num1 / num2;
                break;
        }
        return r;
    }
}