package com.tairanchina.csp.dew.idempotent.strategy;


import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class BloomFilterProcessor implements DewIdempotentProcessor {

    private static final String CACHE_KEY = "dew.idempotent.bloom";

    @PostConstruct
    public void init() {

    }


    @Override
    public StatusEnum getStatus(String optType, String optId) {
        return null;
    }

    @Override
    public boolean storage(String optType, String optId, StatusEnum status, long expireMs) {
        return false;
    }

    @Override
    public boolean confirm(String optType, String optId) {
        return false;
    }

}
