package com.tairanchina.csp.dew.core.entity;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

public abstract class PkEntity implements Serializable {

    @ApiModelProperty("主键")
    @PkColumn
    protected Object id;

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }
}
