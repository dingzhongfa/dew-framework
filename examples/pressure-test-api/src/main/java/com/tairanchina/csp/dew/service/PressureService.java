package com.tairanchina.csp.dew.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by panshuai on 17/6/29.
 */
@FeignClient(value = "pressure-service", fallback = PressureServiceHystrix.class)
public interface PressureService {

    @RequestMapping(value = "/account/{userId}/balance", method = RequestMethod.GET)
    Integer getBalance(@PathVariable(value = "userId") int userId);


    @RequestMapping(value = "/account/transfer", method = RequestMethod.GET)
    Boolean transfer(@RequestParam(value = "turnOutUser") int turnOutUser,
                     @RequestParam(value = "turnIntUser") int turnIntUser,
                     @RequestParam(value = "amount") int amount);

    @RequestMapping(value = "/account/addBalance", method = RequestMethod.GET)
    Boolean addAmount(@RequestParam(value = "userId") int userId,
                       @RequestParam(value = "amount") int amount);
}
