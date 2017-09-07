package com.tairanchina.csp.dew.core;

import com.tairanchina.csp.dew.core.dao.TestInterfaceDao;
import com.tairanchina.csp.dew.core.dao.TestSelectEntity;
import com.tairanchina.csp.dew.core.service.CRUDSService;
import org.springframework.stereotype.Service;

@Service
public class CRUDSTestService implements CRUDSService<TestInterfaceDao, Integer, TestSelectEntity> {

}
