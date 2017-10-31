package com.tairanchina.csp.dew.core.utils.convert;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * desription:
 * Created by ding on 2017/10/31.
 */
@Component
public class StringToLocalTimeConverter implements Converter<String,LocalTime> {

    @Override
    public LocalTime convert(String str) {
        return LocalTime.parse(str, DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

}
