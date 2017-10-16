package com.tairanchina.csp.dew.jdbc.crud.convert;


import com.tairanchina.csp.dew.core.controller.VOAssembler;
import com.tairanchina.csp.dew.jdbc.crud.entity.TestSelectEntity;

public class VOConvert implements VOAssembler<TestSelectEntity,TestSelectEntity> {
    @Override
    public boolean convertAble() {
        return true;
    }
}
