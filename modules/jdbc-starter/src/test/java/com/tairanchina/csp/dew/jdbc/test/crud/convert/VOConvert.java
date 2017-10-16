package com.tairanchina.csp.dew.jdbc.test.crud.convert;


import com.tairanchina.csp.dew.core.controller.VOAssembler;
import com.tairanchina.csp.dew.jdbc.test.crud.entity.TestSelectEntity;

public class VOConvert implements VOAssembler<TestSelectEntity,TestSelectEntity> {
    @Override
    public boolean convertAble() {
        return true;
    }
}
