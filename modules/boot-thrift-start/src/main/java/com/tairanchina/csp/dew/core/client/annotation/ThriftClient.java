package com.tairanchina.csp.dew.core.client.annotation;

import java.lang.annotation.*;

/**
 * according this Annotation to register bean in spring bean factory through ThriftClientAnnotationBeanPostProcessor
 * Created by 迹_Jason on 2017/08/09.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ThriftClient {

    /**
     * the serviceId registered in spring cloud consul or eureka
     *
     * @return
     */
    String serviceId() default "";

    /**
     * thrift access address
     *
     * @return
     */
    String path() default "";
}