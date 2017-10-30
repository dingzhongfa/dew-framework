package com.tairanchina.csp.dew.auth.csp;


import com.tairanchina.csp.dew.core.dto.OptInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(value = "CSP登录信息")
public class CSPOptInfo<E> extends OptInfo<E> {

    @ApiModelProperty(value = "应用Id", required = true)
    protected String appId;
    @ApiModelProperty(value = "角色列表", required = true)
    protected List<String> roles;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
