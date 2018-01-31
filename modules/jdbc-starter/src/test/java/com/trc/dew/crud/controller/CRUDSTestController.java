package com.trc.dew.crud.controller;


import com.tairanchina.csp.dew.core.controller.CRUDSController;
import com.trc.dew.crud.entity.TestSelectEntity;
import com.trc.dew.crud.service.CRUDSTestService;


public class CRUDSTestController implements CRUDSController<CRUDSTestService,Integer, TestSelectEntity> {

    @Override
    public boolean convertAble() {
        return true;
    }


}
