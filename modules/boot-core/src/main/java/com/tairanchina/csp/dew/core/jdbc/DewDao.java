package com.tairanchina.csp.dew.core.jdbc;

import com.ecfront.dew.common.Page;

import java.util.LinkedHashMap;
import java.util.List;

public interface DewDao<E> {

    default String ds() {
        return "";
    }

    long insert(Object entity);

    void insert(Iterable<?> entities);

    void updateById(long id, Object entity);

    void updateByCode(String code, Object entity);

    E getById(long id);

    E getByCode(String code);

    void deleteById(long id);

    void deleteByCode(String code);

    void enableById(long id);

    void enableByCode(String code);

    void disableById(long id);

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

