package com.tairanchina.csp.dew.idempotent.strategy;


import com.tairanchina.csp.dew.Dew;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class ItemProcessor implements DewIdempotentProcessor {

    private static final String CACHE_KEY = "dew.idempotent.item.";

    @Override
    public StatusEnum getStatus(String optType, String optId) {
        String status = Dew.cluster.cache.get(CACHE_KEY + optType + "." + optId);
        if (StringUtils.isEmpty(status)) {
            return StatusEnum.NOT_EXIST;
        }
        return StatusEnum.valueOf(status);
    }

    @Override
    public boolean storage(String optType, String optId, StatusEnum status, long expireMs) {
        Dew.cluster.cache.set(CACHE_KEY + optType + "." + optId, status.toString(), (int) (expireMs / 1000));
        return true;
    }

    @Override
    public boolean confirm(String optType, String optId) {
        return confirm(optType + "." + optId);
    }

    private boolean confirm(String key) {
        Dew.cluster.cache.set(CACHE_KEY + key, StatusEnum.CONFIRMED.toString());
        // todo 补偿
        return true;
    }
}
