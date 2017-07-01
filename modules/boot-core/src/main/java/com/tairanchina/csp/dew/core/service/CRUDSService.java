package com.tairanchina.csp.dew.core.service;

import com.tairanchina.csp.dew.core.jdbc.DewDao;

public interface CRUDSService<T extends DewDao<E>, E> extends CRUSService<T, E>, CRUDService<T, E> {

}
