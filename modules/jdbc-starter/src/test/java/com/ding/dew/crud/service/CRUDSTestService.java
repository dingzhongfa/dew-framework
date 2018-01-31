package com.ding.dew.crud.service;


import com.tairanchina.csp.dew.core.service.CRUDSService;
import com.ding.dew.crud.entity.TestSelectEntity;
import com.ding.dew.select.dao.TestInterfaceDao;
import org.springframework.stereotype.Service;

@Service
public class CRUDSTestService implements CRUDSService<TestInterfaceDao, Integer, TestSelectEntity> {

}
