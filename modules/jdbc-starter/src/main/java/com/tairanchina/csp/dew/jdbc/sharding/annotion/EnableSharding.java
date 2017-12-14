package com.tairanchina.csp.dew.jdbc.sharding.annotion;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * desription:
 * Created by ding on 2017/12/13.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
//@Import()
public @interface EnableSharding {


}
