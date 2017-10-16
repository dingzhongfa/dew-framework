package com.tairanchina.csp.dew.jdbc.crud.controller;


import com.tairanchina.csp.dew.core.controller.CRUDSController;
import com.tairanchina.csp.dew.jdbc.crud.entity.TestSelectEntity;
import com.tairanchina.csp.dew.jdbc.crud.service.CRUDSTestService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/crud/")
public class CRUDSTestController implements CRUDSController<CRUDSTestService,Integer, TestSelectEntity> {

    @Override
    public boolean convertAble() {
        return true;
    }


}
