package com.ding.dew.crud.controller;


import com.tairanchina.csp.dew.core.controller.CRUDSController;
import com.ding.dew.crud.entity.TestSelectEntity;
import com.ding.dew.crud.service.CRUDSTestService;


public class CRUDSTestController implements CRUDSController<CRUDSTestService,Integer, TestSelectEntity> {

    @Override
    public boolean convertAble() {
        return true;
    }


}
