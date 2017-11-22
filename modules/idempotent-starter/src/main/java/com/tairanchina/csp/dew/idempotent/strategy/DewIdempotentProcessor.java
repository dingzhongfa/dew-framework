package com.tairanchina.csp.dew.idempotent.strategy;

public interface DewIdempotentProcessor {

    StatusEnum getStatus(String optType, String optId);

    boolean storage(String optType, String optId, StatusEnum status, long expireMs);

    boolean confirm(String optType, String optId);

}
