package com.tairanchina.csp.dew.core.service;

import com.tairanchina.csp.dew.core.entity.IdEntity;
import com.tairanchina.csp.dew.core.repository.DewRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface DewService<T extends DewRepository<E>, E extends IdEntity> {

    Logger logger = LoggerFactory.getLogger(DewService.class);

    Class<E> getModelClazz();

    T getDewRepository();

}
