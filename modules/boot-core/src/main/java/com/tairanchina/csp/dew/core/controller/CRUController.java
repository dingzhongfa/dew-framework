package com.tairanchina.csp.dew.core.controller;


import com.tairanchina.csp.dew.core.service.CRUService;
import com.tairanchina.csp.dew.core.entity.IdEntity;

public interface CRUController<T extends CRUService, E extends IdEntity> extends CRUVOController<T, E, E> {

}

