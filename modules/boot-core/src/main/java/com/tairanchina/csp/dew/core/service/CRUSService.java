package com.tairanchina.csp.dew.core.service;

import com.ecfront.dew.common.$;
import com.ecfront.dew.common.Page;
import com.ecfront.dew.common.Resp;
import com.tairanchina.csp.dew.core.jdbc.DewDao;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

public interface CRUSService<T extends DewDao<E>, E> extends CRUService<T, E> {

    default Resp<Optional<Object>> preEnableById(long id) throws RuntimeException {
        return Resp.success(Optional.empty());
    }

    default Resp<Optional<Object>> preEnableByCode(String code) throws RuntimeException {
        return Resp.success(Optional.empty());
    }

    default void postEnableById(long id, Optional<Object> preBody) throws RuntimeException {
    }

    default void postEnableByCode(String code, Optional<Object> preBody) throws RuntimeException {
    }

    default Resp<Optional<Object>> preDisableById(long id) throws RuntimeException {
        return Resp.success(Optional.empty());
    }

    default void postDisableById(long id, Optional<Object> preBody) throws RuntimeException {
    }

    default Resp<Optional<Object>> preDisableByCode(String code) throws RuntimeException {
        return Resp.success(Optional.empty());
    }

    default void postDisableByCode(String code, Optional<Object> preBody) throws RuntimeException {
    }

    default Resp<List<E>> findEnabled() throws RuntimeException {
        logger.debug("[{}] FindEnable.", getModelClazz().getSimpleName());
        Resp<Optional<Object>> preResult = preFind();
        if (preResult.ok()) {
            return Resp.success(postFind(getDao().findEnabled(), preResult.getBody()));
        }
        return Resp.customFail(preResult.getCode(), preResult.getMessage());
    }

    default Resp<List<E>> findDisabled() throws RuntimeException {
        logger.debug("[{}] FindDisable.", getModelClazz().getSimpleName());
        Resp<Optional<Object>> preResult = preFind();
        if (preResult.ok()) {
            return Resp.success(postFind(getDao().findDisabled(), preResult.getBody()));
        }
        return Resp.customFail(preResult.getCode(), preResult.getMessage());
    }

    default Resp<Page<E>> pagingEnabled(int pageNumber, int pageSize) throws RuntimeException {
        return pagingEnabled(pageNumber, pageSize, null);
    }

    default Resp<Page<E>> pagingEnabled(int pageNumber, int pageSize, LinkedHashMap<String, Boolean> orderDesc) throws RuntimeException {
        logger.debug("[{}] PagingEnable {} {} {}.", getModelClazz().getSimpleName(), pageNumber, pageSize, orderDesc != null ? $.json.toJsonString(orderDesc) : "");
        Resp<Optional<Object>> preResult = prePaging();
        if (preResult.ok()) {
            return Resp.success(postPaging(getDao().pagingEnabled(pageNumber, pageSize, orderDesc), preResult.getBody()));
        }
        return Resp.customFail(preResult.getCode(), preResult.getMessage());
    }

    default Resp<Page<E>> pagingDisabled(int pageNumber, int pageSize) throws RuntimeException {
        return pagingDisabled(pageNumber, pageSize, null);
    }

    default Resp<Page<E>> pagingDisabled(int pageNumber, int pageSize, LinkedHashMap<String, Boolean> orderDesc) throws RuntimeException {
        logger.debug("[{}] PagingDisable {} {} {}.", getModelClazz().getSimpleName(), pageNumber, pageSize, orderDesc != null ? $.json.toJsonString(orderDesc) : "");
        Resp<Optional<Object>> preResult = prePaging();
        if (preResult.ok()) {
            return Resp.success(postPaging(getDao().pagingDisabled(pageNumber, pageSize, orderDesc), preResult.getBody()));
        }
        return Resp.customFail(preResult.getCode(), preResult.getMessage());
    }

    @Transactional
    default Resp<Void> enableById(long id) throws RuntimeException {
        logger.debug("[{}] EnableById:{}.", getModelClazz().getSimpleName(), id);
        Resp<Optional<Object>> preResult = preEnableById(id);
        if (preResult.ok()) {
            getDao().enableById(id);
            postEnableById(id, preResult.getBody());
            return Resp.success(null);
        }
        return Resp.customFail(preResult.getCode(), preResult.getMessage());
    }

    @Transactional
    default Resp<Void> enableByCode(String code) throws RuntimeException {
        logger.debug("[{}] EnableByCode:{}.", getModelClazz().getSimpleName(), code);
        Resp<Optional<Object>> preResult = preEnableByCode(code);
        if (preResult.ok()) {
            getDao().enableByCode(code);
            postEnableByCode(code, preResult.getBody());
            return Resp.success(null);
        }
        return Resp.customFail(preResult.getCode(), preResult.getMessage());
    }

    @Transactional
    default Resp<Void> disableById(long id) throws RuntimeException {
        logger.debug("[{}] DisableById:{}.", getModelClazz().getSimpleName(), id);
        Resp<Optional<Object>> preResult = preDisableById(id);
        if (preResult.ok()) {
            getDao().disableById(id);
            postDisableById(id, preResult.getBody());
            return Resp.success(null);
        }
        return Resp.customFail(preResult.getCode(), preResult.getMessage());
    }

    @Transactional
    default Resp<Void> disableByCode(String code) throws RuntimeException {
        logger.debug("[{}] DisableByCode:{}.", getModelClazz().getSimpleName(), code);
        Resp<Optional<Object>> preResult = preDisableByCode(code);
        if (preResult.ok()) {
            getDao().disableByCode(code);
            postDisableByCode(code, preResult.getBody());
            return Resp.success(null);
        }
        return Resp.customFail(preResult.getCode(), preResult.getMessage());
    }

}
