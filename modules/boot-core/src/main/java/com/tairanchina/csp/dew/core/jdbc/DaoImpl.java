package com.tairanchina.csp.dew.core.jdbc;

import java.lang.reflect.ParameterizedType;

@Deprecated
public class DaoImpl<E> implements DewDao<E> {

    @Override
    public Class<E> getClazz() {
        return (Class<E>) (((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
    }

}
