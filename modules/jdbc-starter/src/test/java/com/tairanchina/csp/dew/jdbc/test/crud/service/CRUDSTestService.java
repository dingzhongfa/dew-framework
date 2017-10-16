package com.tairanchina.csp.dew.jdbc.test.crud.service;


import com.tairanchina.csp.dew.core.service.CRUDSService;
import com.tairanchina.csp.dew.jdbc.test.crud.entity.TestSelectEntity;
import com.tairanchina.csp.dew.jdbc.test.select.dao.TestInterfaceDao;
import org.springframework.stereotype.Service;

@Service
public class CRUDSTestService implements CRUDSService<TestInterfaceDao, Integer, TestSelectEntity> {

}
