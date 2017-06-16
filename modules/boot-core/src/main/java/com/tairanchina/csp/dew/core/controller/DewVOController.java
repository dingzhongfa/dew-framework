package com.tairanchina.csp.dew.core.controller;


import com.tairanchina.csp.dew.core.service.DewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface DewVOController<T extends DewService, V, E> extends VOAssembler<V, E> {

    Logger logger = LoggerFactory.getLogger(DewVOController.class);

    T getDewService();

}

