package com.tairanchina.csp.dew.core.controller;


import com.tairanchina.csp.dew.core.entity.IdEntity;
import com.tairanchina.csp.dew.core.service.CRUSService;

public interface CRUSController<T extends CRUSService, E extends IdEntity> extends CRUSVOController<T, E, E> {

}

