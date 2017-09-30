package com.tairanchina.csp.dew.core.test.crud.service;

import com.tairanchina.csp.dew.core.test.dataaccess.select.dao.TestInterfaceDao;
import com.tairanchina.csp.dew.core.test.crud.entity.TestSelectEntity;
import com.tairanchina.csp.dew.core.service.CRUDSService;
import org.springframework.stereotype.Service;

@Service
public class CRUDSTestService implements CRUDSService<TestInterfaceDao, Integer, TestSelectEntity> {

}
