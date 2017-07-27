package com.tairanchina.csp.dew.core.jdbc.annotations;

import java.lang.annotation.*;

/**
 * Created by 迹_Jason on 2017/7/21.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Select {
    String value() default "";
    Class<?> entityClass();
}
