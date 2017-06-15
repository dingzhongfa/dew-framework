package com.tairanchina.csp.dew.core.entity;

import io.swagger.annotations.ApiModelProperty;

public abstract class StatusEntity extends IdEntity {

    @ApiModelProperty("是否启用")
    protected Boolean enable;

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

}
