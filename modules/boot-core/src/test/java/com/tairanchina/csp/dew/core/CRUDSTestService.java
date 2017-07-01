package com.tairanchina.csp.dew.core;

import com.tairanchina.csp.dew.core.service.CRUDSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CRUDSTestService implements CRUDSService<CRUDSTestDao, CRUDSTestEntity> {

    @Autowired
    private CRUDSTestDao crudsTestDao;

    @Override
    public Class<CRUDSTestEntity> getModelClazz() {
        return CRUDSTestEntity.class;
    }

    @Override
    public CRUDSTestDao getDao() {
        return crudsTestDao;
    }
}
