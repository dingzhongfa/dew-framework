package com.tairanchina.csp.dew.core.service;

import com.ecfront.dew.common.Resp;
import com.tairanchina.csp.dew.core.jdbc.DewDao;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface CRUDService<T extends DewDao<E>, E> extends CRUService<T, E> {

    default Resp<Optional<Object>> preDeleteById(long id) throws RuntimeException {
        return Resp.success(Optional.empty());
    }

    default void postDeleteById(long id, Optional<Object> preBody) throws RuntimeException {
    }

    default Resp<Optional<Object>> preDeleteByCode(String code) throws RuntimeException {
        return Resp.success(Optional.empty());
    }

    default void postDeleteByCode(String code, Optional<Object> preBody) throws RuntimeException {
    }

    @Transactional
    default Resp<Void> deleteById(long id) throws RuntimeException {
        logger.debug("[{}] DeleteById:{}.", getModelClazz().getSimpleName(), id);
        Resp<Optional<Object>> preResult = preDeleteById(id);
        if (preResult.ok()) {
            getDao().deleteById(id);
            postDeleteById(id, preResult.getBody());
            return Resp.success(null);
        }
        return Resp.customFail(preResult.getCode(), preResult.getMessage());
    }

    @Transactional
    default Resp<Void> deleteByCode(String code) throws RuntimeException {
        logger.debug("[{}] DeleteByCode:{}.", getModelClazz().getSimpleName(), code);
        Resp<Optional<Object>> preResult = preDeleteByCode(code);
        if (preResult.ok()) {
            getDao().deleteByCode(code);
            postDeleteByCode(code, preResult.getBody());
            return Resp.success(null);
        }
        return Resp.customFail(preResult.getCode(), preResult.getMessage());
    }

}
