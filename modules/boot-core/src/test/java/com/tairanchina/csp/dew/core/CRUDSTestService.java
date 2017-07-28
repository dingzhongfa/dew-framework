package com.tairanchina.csp.dew.core;

import com.tairanchina.csp.dew.core.dao.TestInterfaceDao;
import com.tairanchina.csp.dew.core.service.CRUDSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CRUDSTestService implements CRUDSService<TestInterfaceDao, CRUDSTestEntity> {

    @Autowired
    private TestInterfaceDao crudsTestDao;

    @Override
    public Class<CRUDSTestEntity> getModelClazz() {
        return CRUDSTestEntity.class;
    }

    @Override
    public TestInterfaceDao getDao() {
        return crudsTestDao;
    }
}
