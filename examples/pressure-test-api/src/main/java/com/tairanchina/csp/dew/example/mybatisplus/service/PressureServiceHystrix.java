package com.tairanchina.csp.dew.example.mybatisplus.service;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by panshuai on 17/6/29.
 */
@Component
public class PressureServiceHystrix implements PressureService{

    @Override
    public Integer getBalance(@PathVariable(value = "userId") int userId) {
        return -9999;
    }

    @Override
    public Boolean transfer(@RequestParam(value = "turnOutUser") int turnOutUser, @RequestParam(value = "turnIntUser") int turnIntUser, @RequestParam(value = "amount") int amount) {
        return Boolean.FALSE;
    }

    @Override
    public Boolean addAmount(@RequestParam(value = "userId") int userId, @RequestParam(value = "amount") int amount) {
        return Boolean.FALSE;
    }
}
