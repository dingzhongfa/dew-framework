package com.tairanchina.csp.dew.core.controller;


import com.tairanchina.csp.dew.core.service.CRUDSService;

public interface CRUDSController<T extends CRUDSService, E> extends CRUDController<T, E>, CRUSController<T, E> {

    @Override
    default boolean convertAble() {
        return false;
    }

}

