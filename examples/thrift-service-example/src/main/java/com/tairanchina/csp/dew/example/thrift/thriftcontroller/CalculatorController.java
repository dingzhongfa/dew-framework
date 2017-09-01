package com.tairanchina.csp.dew.example.thrift.thriftcontroller;

import com.tairanchina.csp.dew.core.server.annotation.ThriftService;
import com.tairanchina.csp.dew.example.thrift.TCalculatorService;
import com.tairanchina.csp.dew.example.thrift.TDivisionByZeroException;
import com.tairanchina.csp.dew.example.thrift.TOperation;
import org.apache.thrift.TException;

import java.util.Random;

/**
 * Created by 迹_Jason on 2017/08/09.
 */
@ThriftService(value = "/thriftcaclcu")
public class CalculatorController implements TCalculatorService.Iface {

    @Override
    public int calculate(int num1, int num2, TOperation op) throws TDivisionByZeroException, TException {
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