package com.tairanchina.csp.dew.core;

import com.tairanchina.csp.dew.core.controller.CRUDSController;
import com.tairanchina.csp.dew.core.service.CRUDSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/crud/")
public class CRUDSTestController implements CRUDSController<CRUDSTestService, CRUDSTestEntity> {

    @Autowired
    private CRUDSTestService crudsTestService;


    @Override
    public CRUDSTestService getDewService() {
        return crudsTestService;
    }
}
