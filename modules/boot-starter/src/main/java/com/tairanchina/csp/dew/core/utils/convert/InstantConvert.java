package com.tairanchina.csp.dew.core.utils.convert;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.Instant;

/**
 * desription:
 * Created by ding on 2017/11/5.
 */
@Component
public class InstantConvert implements Converter<String,Instant> {
    @Override
    public Instant convert(String str) {
        return Instant.ofEpochMilli(Long.valueOf(str));
    }
}
