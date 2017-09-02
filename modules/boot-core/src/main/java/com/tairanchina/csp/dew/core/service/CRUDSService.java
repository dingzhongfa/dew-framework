package com.tairanchina.csp.dew.core.service;

import com.tairanchina.csp.dew.core.jdbc.DewDao;

public interface CRUDSService<T extends DewDao<P, E>, P, E> extends CRUSService<T, P, E>, CRUDService<T, P, E> {

}
