package com.tairanchina.csp.dew.core.controller;


import com.tairanchina.csp.dew.core.service.CRUDSService;

public interface CRUDSVOController<T extends CRUDSService, V, E> extends CRUDVOController<T, V, E>, CRUSVOController<T, V, E> {

    @Override
    default boolean convertAble() {
        return true;
    }

}

