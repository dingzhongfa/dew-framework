package com.tairanchina.csp.dew.idempotent;

import com.ecfront.dew.common.Resp;
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

    public static Resp<DewIdempotentProcessor> initProcessor(String storageStrategy, String optType, String optId) {
        DewIdempotentProcessor processor;
        if (storageStrategy.equalsIgnoreCase("item")) {
            processor = itemProcessor;
        } else if (storageStrategy.equalsIgnoreCase("bloom")) {
            processor = bloomFilterProcessor;
        } else {
            return Resp.badRequest("Idempotent storage strategy NOT exist.");
        }
        CONTENT.putIfAbsent(optType, processor);
        CONTEXT.set(new String[]{optType, optId});
        return Resp.success(processor);
    }

    /**
     * 操作确认
     * @param optType 操作类型
     * @param optId 操作ID
     * @return
     */
    public static boolean confirm(String optType, String optId) {
        return CONTENT.get(optType).confirm(optType, optId);
    }

    /**
     * 操作确认，要求与请求入口在同一线程中
     * @return 确认成功或失败
     */
    public static boolean confirm() {
        String[] c = CONTEXT.get();
        return CONTENT.get(c[0]).confirm(c[0], c[1]);
    }

}
