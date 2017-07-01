package com.tairanchina.csp.dew.core.service;

import com.tairanchina.csp.dew.core.jdbc.DewDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface DewService<T extends DewDao<E>, E> {

    Logger logger = LoggerFactory.getLogger(DewService.class);

    Class<E> getModelClazz();

    T getDao();

}
