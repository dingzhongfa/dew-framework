package com.tairanchina.csp.dew.controller;

import com.tairanchina.csp.dew.example.mybatisplus.service.AccountService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by panshuai on 17/6/29.
 */
@RestController
public class AccountController {

    private Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private AccountService accountService;


    @RequestMapping(value = "/account/{userId}/balance", method = RequestMethod.GET)
    public int add(@PathVariable(value = "userId") int userId){
        try {
            Thread.sleep(5000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(userId <= 0){
            return 0;
        }
        return accountService.getAmount(userId);
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public Integer test() {
        try {
            logger.info("enter test");
            Thread.sleep(10000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @RequestMapping(value = "/account/transfer", method = RequestMethod.GET)
    public boolean transfer(@RequestParam(value = "turnOutUser") int turnOutUser,
                            @RequestParam(value = "turnIntUser") int turnIntUser,
                            @RequestParam(value = "amount") int amount){
        if(turnOutUser <=0
                || turnIntUser <= 0
                || amount <= 0){
            return Boolean.FALSE;
        }
        try {
            return accountService.transfer(turnOutUser, turnIntUser, amount);
        } catch (Exception e){
//            try{
//                //重试一次
//                return accountService.transfer(turnOutUser, turnIntUser, amount);
//            } catch (Exception ex){
//                return Boolean.FALSE;
//            }
            return Boolean.FALSE;
        }

    }

    @RequestMapping(value = "/account/addBalance", method = RequestMethod.GET)
    public Boolean addAmount(@RequestParam(value = "userId") int userId,
                   @RequestParam(value = "amount") int amount){
        if(userId <= 0 || amount <= 0){
            return Boolean.FALSE;
        }
        try {
            return accountService.addAmount(userId, amount);
        } catch (Exception e){
            return Boolean.FALSE;
        }
    }
}
