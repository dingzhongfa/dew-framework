package com.tairanchina.csp.dew.example.jdbc;

import com.tairanchina.csp.dew.core.entity.Column;
import com.tairanchina.csp.dew.core.entity.Entity;
import com.tairanchina.csp.dew.core.entity.PkEntity;

@Entity
public  class ExampleEntity extends PkEntity {

    @Column
    private String fieldA;

    public String getFieldA() {
        return fieldA;
    }

    public void setFieldA(String fieldA) {
        this.fieldA = fieldA;
    }

}