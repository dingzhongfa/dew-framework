package com.tairanchina.csp.dew.example.thrift.client;

import com.tairanchina.csp.dew.example.thrift.TCalculatorService;
import com.tairanchina.csp.dew.example.thrift.TOperation;
import com.tairanchina.csp.dew.rpc.thrift.client.annotation.ThriftClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class CalculatorClientController {

    @ThriftClient(serviceId = "thrift-process-example", path = "/thriftcaclcu")
    TCalculatorService.Client tcalculatorClient;

    @GetMapping("/httpcaclcu/{num1}/{num2}/{operation}")
    public Map<String, Object> caclcu(@PathVariable("num1") int m, @PathVariable("num2") int n,
                         @PathVariable("operation") String op) throws Exception {
        Map<String, Object> o = new HashMap<>();
        switch (op) {
            case "1":
                o.put(op, tcalculatorClient.calculate(m, n, TOperation.ADD));
                break;
            case "2":
                o.put(op, tcalculatorClient.calculate(m, n, TOperation.SUBTRACT));
                break;
            case "3":
                o.put(op, tcalculatorClient.calculate(m, n, TOperation.MULTIPLY));
                break;
            case "4":
                o.put(op, tcalculatorClient.calculate(m, n, TOperation.DIVIDE));
                break;
            default:
                o.put(op, op);
                break;
        }
        return o;
    }

    @GetMapping("/lhttpcaclcu/{num1}/{num2}/{operation}")
    public Map<String, Object> caclculocal(@PathVariable("num1") int m, @PathVariable("num2") int n,
                                           @PathVariable("operation") String op) throws Exception {
        Map<String, Object> o = new HashMap<>();
        switch (op) {
            case "1":
                o.put(op, m + n);
                break;
            case "2":
                o.put(op, m - n);
                break;
            case "3":
                o.put(op, m * n);
                break;
            case "4":
                o.put(op, m / n);
                break;
            default:
                o.put(op, op);
                break;
        }
        return o;
    }

}