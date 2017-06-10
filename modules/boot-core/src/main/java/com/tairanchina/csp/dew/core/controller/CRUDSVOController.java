package com.tairanchina.csp.dew.core.controller;


import com.tairanchina.csp.dew.core.entity.IdEntity;
import com.tairanchina.csp.dew.core.service.CRUDSService;

public interface CRUDSVOController<T extends CRUDSService, V extends Object, E extends IdEntity> extends CRUDVOController<T, V, E>, CRUSVOController<T, V, E> {

}

