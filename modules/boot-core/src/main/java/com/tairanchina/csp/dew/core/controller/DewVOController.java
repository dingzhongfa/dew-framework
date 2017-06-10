package com.tairanchina.csp.dew.core.controller;


import com.tairanchina.csp.dew.core.entity.IdEntity;
import com.tairanchina.csp.dew.core.service.DewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface DewVOController<T extends DewService, V extends Object, E extends IdEntity> extends VOAssembler<V, E> {

    Logger logger = LoggerFactory.getLogger(DewVOController.class);

    T getDewService();

}

