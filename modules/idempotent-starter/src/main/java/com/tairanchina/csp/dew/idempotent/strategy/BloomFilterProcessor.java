package com.tairanchina.csp.dew.idempotent.strategy;


import org.springframework.stereotype.Component;

@Component
public class BloomFilterProcessor implements DewIdempotentProcessor {

    @Override
    public StatusEnum process(String optType, String optId, StatusEnum initStatus, long expireMs) {
        return null;
    }

    @Override
    public boolean confirm(String optType, String optId) {
        return false;
    }

    @Override
    public boolean cancel(String optType, String optId) {
        return false;
    }
}
