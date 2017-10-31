package com.tairanchina.csp.dew.core.utils.convert;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * desription:
 * Created by ding on 2017/10/31.
 */
@Component
public class StringToLocalDateTimeConverter implements Converter<String, LocalDateTime> {
    @Override
    public LocalDateTime convert(String str) {
        if (str.matches("[1-9][0-9]+")){
            return LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.valueOf(str)), ZoneId.of("Asia/Shanghai"));
        }
        return LocalDateTime.parse(str, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
