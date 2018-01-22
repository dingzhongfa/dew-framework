package com.tairanchina.csp.dew.core.metric;

/**
 * desription:
 * Created by ding on 2018/1/22.
 */
public enum RequestType {

    ERROR("异常请求"),NORMAL("正常请求");

    private String description;

    RequestType(String description){
        this.description =description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
