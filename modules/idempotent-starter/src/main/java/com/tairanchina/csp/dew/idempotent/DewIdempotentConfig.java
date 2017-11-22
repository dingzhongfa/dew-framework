package com.tairanchina.csp.dew.idempotent;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "dew.idempotent")
public class DewIdempotentConfig {

    // 设置默认过期时间，1小时
    private long defaultExpireMs = 3600000;
    // 设置默认幂等记录是否需要手工确认（防止收到请求后但内部处理错误导致无法重试），需要确认
    private boolean defaultNeedConfirm = true;
    // 设置默认存储策略，支持 bloom(Bloom Filter)和item(逐条记录)
    private String defaultStorageStrategy = "item";
    // 指定幂等操作类型标识，可以位于HTTP Header或请求参数中
    private String optTypeFlag = "__IDEMPOTENT_OPT_TYPE__";
    // 指定幂等操作ID标识，可以位于HTTP Header或请求参数中
    private String optIdFlag = "__IDEMPOTENT_OPT_ID__";
    // 指定操作级的过期时间标识，可以位于HTTP Header或请求参数中
    private String optExpireMsFlag = "__IDEMPOTENT_EXPIRE_MS__";
    // 指定操作级的存储策略标识，可以位于HTTP Header或请求参数中
    private String optStorageStrategyFlag = "__IDEMPOTENT_SS__";
    // 指定操作级的是否需要手工确认标识，可以位于HTTP Header或请求参数中
    private String optNeedConfirmFlag = "__IDEMPOTENT_CONFIRM__";
    // 指定是否强制忽略幂等检查标识，可以位于HTTP Header或请求参数中
    private String forceIgnoreCheckFlag = "__IDEMPOTENT_FORCE__";

    public long getDefaultExpireMs() {
        return defaultExpireMs;
    }

    public void setDefaultExpireMs(long defaultExpireMs) {
        this.defaultExpireMs = defaultExpireMs;
    }

    public boolean isDefaultNeedConfirm() {
        return defaultNeedConfirm;
    }

    public void setDefaultNeedConfirm(boolean defaultNeedConfirm) {
        this.defaultNeedConfirm = defaultNeedConfirm;
    }

    public String getDefaultStorageStrategy() {
        return defaultStorageStrategy;
    }

    public void setDefaultStorageStrategy(String defaultStorageStrategy) {
        this.defaultStorageStrategy = defaultStorageStrategy;
    }

    public String getOptTypeFlag() {
        return optTypeFlag;
    }

    public void setOptTypeFlag(String optTypeFlag) {
        this.optTypeFlag = optTypeFlag;
    }

    public String getOptIdFlag() {
        return optIdFlag;
    }

    public void setOptIdFlag(String optIdFlag) {
        this.optIdFlag = optIdFlag;
    }

    public String getOptExpireMsFlag() {
        return optExpireMsFlag;
    }

    public void setOptExpireMsFlag(String optExpireMsFlag) {
        this.optExpireMsFlag = optExpireMsFlag;
    }

    public String getOptStorageStrategyFlag() {
        return optStorageStrategyFlag;
    }

    public void setOptStorageStrategyFlag(String optStorageStrategyFlag) {
        this.optStorageStrategyFlag = optStorageStrategyFlag;
    }

    public String getOptNeedConfirmFlag() {
        return optNeedConfirmFlag;
    }

    public void setOptNeedConfirmFlag(String optNeedConfirmFlag) {
        this.optNeedConfirmFlag = optNeedConfirmFlag;
    }

    public String getForceIgnoreCheckFlag() {
        return forceIgnoreCheckFlag;
    }

    public void setForceIgnoreCheckFlag(String forceIgnoreCheckFlag) {
        this.forceIgnoreCheckFlag = forceIgnoreCheckFlag;
    }
}
