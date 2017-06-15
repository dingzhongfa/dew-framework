package com.tairanchina.csp.dew.core.service;

import com.tairanchina.csp.dew.core.entity.IdEntity;
import com.tairanchina.csp.dew.core.jdbc.DewRepository;

public interface CRUDSService<T extends DewRepository<E>, E extends IdEntity> extends CRUSService<T, E>, CRUDService<T, E> {

}
