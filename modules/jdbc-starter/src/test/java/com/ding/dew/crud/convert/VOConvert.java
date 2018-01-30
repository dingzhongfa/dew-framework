package com.ding.dew.crud.convert;


import com.tairanchina.csp.dew.core.controller.VOAssembler;
import com.ding.dew.crud.entity.TestSelectEntity;

public class VOConvert implements VOAssembler<TestSelectEntity,TestSelectEntity> {
    @Override
    public boolean convertAble() {
        return true;
    }
}
