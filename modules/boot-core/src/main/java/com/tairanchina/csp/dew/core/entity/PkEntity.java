package com.tairanchina.csp.dew.core.entity;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

public abstract class PkEntity implements Serializable {

    @ApiModelProperty("主键")
    @PkColumn
    protected long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
