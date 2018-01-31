package com.tairanchina.csp.dew.core.utils.convert;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * desription:
 * Created by ding on 2018/1/25.
 */
@Configuration
public class ConvertConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public InstantConvert instantConvert(){
        return new InstantConvert();
    }

    @Bean
    @ConditionalOnMissingBean
    public LocalDateConverter localDateConverter(){
        return new LocalDateConverter();
    }

    @Bean
    @ConditionalOnMissingBean
    public LocalTimeConverter localTimeConverter(){
        return new LocalTimeConverter();
    }


    @Bean
    @ConditionalOnMissingBean
    public LocalDateTimeConverter localDateTimeConverter(){
        return new LocalDateTimeConverter();
    }

}
