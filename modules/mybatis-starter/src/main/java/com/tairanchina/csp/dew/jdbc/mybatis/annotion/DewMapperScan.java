package com.tairanchina.csp.dew.jdbc.mybatis.annotion;

import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * desription:
 * Created by ding on 2017/12/28.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(DewMapperScannerRegister.class)
public @interface DewMapperScan {

    // alias for basePackags ,both have same function
    String[] value() default {};

    String[] basePackages() default {};

    Class<?>[] basePackageClasses() default {};

    // We want the prefix to be the same as the database instance name
    String secondPrefix() default "";

    // support sharding
    boolean enableSharding() default false;

    Class<? extends MapperFactoryBean> factoryBean() default MapperFactoryBean.class;
}
