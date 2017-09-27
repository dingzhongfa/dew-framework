package com.tairanchina.csp.dew.core.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

@ApiModel(value = "登录信息")
public class OptInfo {

    @ApiModelProperty(value = "Token", required = true)
    protected String token;
    @ApiModelProperty(value = "账号编码", required = true)
    protected Object accountCode;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Object getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(Object accountCode) {
        this.accountCode = accountCode;
    }
}
