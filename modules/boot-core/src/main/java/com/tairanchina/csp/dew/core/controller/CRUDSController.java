package com.tairanchina.csp.dew.core.controller;


import com.tairanchina.csp.dew.core.entity.IdEntity;
import com.tairanchina.csp.dew.core.service.CRUDSService;

public interface CRUDSController<T extends CRUDSService, E extends IdEntity> extends CRUDController<T, E>, CRUSController<T, E> {

}

