package com.tairanchina.csp.dew.controller;

import com.tairanchina.csp.dew.example.mybatisplus.service.PressureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by panshuai on 17/6/29.
 */
@RestController
public class PressureClient {

    @Autowired
    private PressureService pressureService;

    @RequestMapping(value = "/account/{userId}/balance", method = RequestMethod.GET)
    public int getBalance(@PathVariable(value = "userId") int userId){
        return pressureService.getBalance(userId);
    }

    @RequestMapping(value = "/account/transfer", method = RequestMethod.GET)
    public boolean transfer(@RequestParam(value = "turnOutUser") int turnOutUser,
                        @RequestParam(value = "turnIntUser") int turnIntUser,
                        @RequestParam(value = "amount") int amount){
        return pressureService.transfer(turnOutUser, turnIntUser, amount);
    }

    @RequestMapping(value = "/account/addBalance", method = RequestMethod.GET)
    public Boolean addAmount(@RequestParam(value = "userId") int userId,
                       @RequestParam(value = "amount") int amount){
        return pressureService.addAmount(userId, amount);
    }
}
