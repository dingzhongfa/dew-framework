package com.tairanchina.csp.dew.core;

import com.tairanchina.csp.dew.core.entity.CodeColumn;
import com.tairanchina.csp.dew.core.entity.Column;
import com.tairanchina.csp.dew.core.entity.Entity;
import com.tairanchina.csp.dew.core.entity.SafeStatusEntity;

@Entity(tableName = "t_full_entity")
public class FullEntity extends SafeStatusEntity<Integer> {

    @CodeColumn
    private String code;
    @Column
    private String fieldA;
    @Column(columnName = "field_c", notNull = true)
    private String fieldB;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFieldA() {
        return fieldA;
    }

    public void setFieldA(String fieldA) {
        this.fieldA = fieldA;
    }

    public String getFieldB() {
        return fieldB;
    }

    public void setFieldB(String fieldB) {
        this.fieldB = fieldB;
    }
}
