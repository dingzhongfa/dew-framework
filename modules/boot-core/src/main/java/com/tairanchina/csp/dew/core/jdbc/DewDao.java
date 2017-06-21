package com.tairanchina.csp.dew.core.jdbc;

import com.ecfront.dew.common.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;

public interface DewDao<E> {

    @Transactional
    long insert(Object entity);

    @Transactional
    void insert(Iterable<?> entities);

    @Transactional
    void updateById(long id, Object entity);

    @Transactional
    void updateByCode(String code, Object entity);

    E getById(long id);

    E getByCode(String code);

    @Transactional
    void deleteById(long id);

    @Transactional
    void deleteByCode(String code);

    @Transactional
    void enableById(long id);

    @Transactional
    void enableByCode(String code);

    @Transactional
    void disableById(long id);

    @Transactional
    void disableByCode(String code);

    boolean existById(long id);

    boolean existByCode(String code);

    default List<E> findAll() {
        return findAll(null);
    }

    List<E> findAll(LinkedHashMap<String, Boolean> orderDesc);

    default List<E> findEnabled() {
        return findEnabled(null);
    }

    List<E> findEnabled(LinkedHashMap<String, Boolean> orderDesc);

    default List<E> findDisabled() {
        return findDisabled(null);
    }

    List<E> findDisabled(LinkedHashMap<String, Boolean> orderDesc);

    long countAll();

    long countEnabled();

    long countDisabled();

    default Page<E> paging(long pageNumber, int pageSize) {
        return paging(pageNumber, pageSize, null);
    }

    Page<E> paging(long pageNumber, int pageSize, LinkedHashMap<String, Boolean> orderDesc);

    default Page<E> pagingEnabled(int pageNumber, int pageSize) {
        return pagingEnabled(pageNumber, pageSize, null);
    }

    Page<E> pagingEnabled(int pageNumber, int pageSize, LinkedHashMap<String, Boolean> orderDesc);

    default Page<E> pagingDisabled(int pageNumber, int pageSize) {
        return pagingDisabled(pageNumber, pageSize, null);
    }

    Page<E> pagingDisabled(int pageNumber, int pageSize, LinkedHashMap<String, Boolean> orderDesc);

}

