package com.tairanchina.csp.dew.core;

import com.tairanchina.csp.dew.core.controller.CRUDSController;
import com.tairanchina.csp.dew.core.dao.TestSelectEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/crud/")
public class CRUDSTestController implements CRUDSController<CRUDSTestService,Integer, TestSelectEntity> {

}
