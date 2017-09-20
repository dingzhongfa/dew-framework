package com.tairanchina.csp.dew.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * TestController
 *
 * @author hzzjb
 * @date 2017/9/19
 */
@RestController
@RequestMapping("performance")
public class TestController {

    private final Logger logger = LoggerFactory.getLogger(TestController.class);

    /**
     *  收到get请求立马返回
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public String getAtOnce() {
        long st = System.currentTimeMillis();
        try {
            return "get at once";
        } finally {
            long et = System.currentTimeMillis();
            logger.info("get at once used ms :" + (et - st));
        }
    }

    /**
     * 收到get请求延迟后再返回
     * @param delayTime 延迟时间毫秒
     */
    @RequestMapping(value = "delay/{time}", method = RequestMethod.GET)
    @ResponseBody
    public String getWithDelay(@PathVariable("time") String delayTime) {
        long st = System.currentTimeMillis();
        try {
            long delay;
            try {
                delay = Integer.valueOf(delayTime);
            } catch (Exception e) {
                logger.error("parse delay time error", e);
                delay = 1000;
            }
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                logger.error("sleep error", e);
            }
            return "get with delay";
        } finally {
            long et = System.currentTimeMillis();
            logger.info("get with delay used ms :" + (et - st));
        }
    }

    /**
     *  收到post请求立马返回
     */
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public String postAtOnce() {
        long st = System.currentTimeMillis();
        try {
            return "post at once";
        } finally {
            long et = System.currentTimeMillis();
            logger.info("post at once used ms :" + (et - st));
        }
    }

    /**
     * 收到post请求延迟后再返回
     * @param time 延迟时间毫秒
     */
    @RequestMapping(value = "delay", method = RequestMethod.POST)
    @ResponseBody
    public String postWithDelay(String time) {
        long st = System.currentTimeMillis();
        try {
            long delay;
            try {
                delay = Integer.valueOf(time);
            } catch (Exception e) {
                logger.error("parse delay time error", e);
                delay = 1000;
            }
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                logger.error("sleep error", e);
            }
            return "post with delay";
        } finally {
            long et = System.currentTimeMillis();
            logger.info("post with delay used ms :" + (et - st));
        }
    }
}
