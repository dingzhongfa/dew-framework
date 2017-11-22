package com.tairanchina.csp.dew.idempotent;

import com.tairanchina.csp.dew.Dew;
import com.tairanchina.csp.dew.idempotent.strategy.BloomFilterProcessor;
import com.tairanchina.csp.dew.idempotent.strategy.DewIdempotentProcessor;
import com.tairanchina.csp.dew.idempotent.strategy.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.validation.ValidationException;
import java.util.HashMap;
import java.util.Map;

@Component
public class DewIdempotent {

    // optType -> DewIdempotentProcessor
    private static final Map<String, DewIdempotentProcessor> CONTENT = new HashMap<>();

    // optType,optId
    private static final ThreadLocal<String[]> CONTEXT = new ThreadLocal<>();

    @Autowired
    @Qualifier("itemProcessor")
    private ItemProcessor innerItemProcessor;

    @Autowired
    @Qualifier("bloomFilterProcessor")
    private BloomFilterProcessor innerBloomFilterProcessor;

    private static ItemProcessor itemProcessor;
    private static BloomFilterProcessor bloomFilterProcessor;

    @PostConstruct
    private void init() {
        itemProcessor = innerItemProcessor;
        bloomFilterProcessor = innerBloomFilterProcessor;
    }

    public static DewIdempotentProcessor initProcessor(String storageStrategy, String optType, String optId) {
        DewIdempotentProcessor processor;
        if (storageStrategy.equalsIgnoreCase("item")) {
            processor = itemProcessor;
        } else if (storageStrategy.equalsIgnoreCase("bloom")) {
            processor = bloomFilterProcessor;
        } else {
            throw Dew.E.e("400", new ValidationException("Idempotent storage strategy NOT exist."), 400);
        }
        CONTENT.putIfAbsent(optType, processor);
        CONTEXT.set(new String[]{optType, optId});
        return processor;
    }

    public static boolean confirm(String optType, String optId) {
        return CONTENT.get(optType).confirm(optType, optId);
    }

    public static boolean confirm() {
        String[] c = CONTEXT.get();
        return CONTENT.get(c[0]).confirm(c[0], c[1]);
    }

}
