package com.tairanchina.csp.dew.core.controller;


import com.tairanchina.csp.dew.core.service.CRUDService;

public interface CRUDController<T extends CRUDService, E> extends CRUDVOController<T, E, E> {

    @Override
    default boolean convertAble() {
        return false;
    }

}

