package com.tairanchina.csp.dew.core.entity;

import io.swagger.annotations.ApiModelProperty;

public abstract class StatusEntity extends PkEntity {

    @ApiModelProperty("是否启用")
    @EnabledColumn
    protected Boolean enable = true;

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

}
