package com.tairanchina.csp.dew.core.entity;

import io.swagger.annotations.ApiModelProperty;

public abstract class IdEntity extends EmptyEntity {

    @ApiModelProperty("主键")
    protected long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
