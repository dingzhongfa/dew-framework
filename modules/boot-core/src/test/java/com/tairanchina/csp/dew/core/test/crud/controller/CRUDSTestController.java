package com.tairanchina.csp.dew.core.test.crud.controller;

import com.tairanchina.csp.dew.core.test.crud.service.CRUDSTestService;
import com.tairanchina.csp.dew.core.controller.CRUDSController;
import com.tairanchina.csp.dew.core.test.select.entity.TestSelectEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/crud/")
public class CRUDSTestController implements CRUDSController<CRUDSTestService,Integer, TestSelectEntity> {

}
