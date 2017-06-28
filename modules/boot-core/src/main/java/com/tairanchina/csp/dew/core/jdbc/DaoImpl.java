package com.tairanchina.csp.dew.core.jdbc;

import com.ecfront.dew.common.Page;
import com.tairanchina.csp.dew.core.Dew;

import java.lang.reflect.ParameterizedType;
import java.util.LinkedHashMap;
import java.util.List;

public abstract class DaoImpl<E> implements DewDao<E> {

    private Class<E> clazz = (Class<E>) (((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]);

    @Override
    public long insert(Object entity) {
        return Dew.ds(ds()).insert(entity);
    }

    @Override
    public void insert(Iterable<?> entities) {
        Dew.ds(ds()).insert(entities);
    }

    @Override
    public void updateById(long id, Object entity) {
        Dew.ds(ds()).updateById(id, entity);
    }

    @Override
    public void updateByCode(String code, Object entity) {
        Dew.ds(ds()).updateByCode(code, entity);
    }

    @Override
    public E getById(long id) {
        return Dew.ds(ds()).getById(id, clazz);
    }

    @Override
    public E getByCode(String code) {
        return Dew.ds(ds()).getByCode(code, clazz);
    }

    @Override
    public void deleteById(long id) {
        Dew.ds(ds()).deleteById(id, clazz);
    }

    @Override
    public void deleteByCode(String code) {
        Dew.ds(ds()).deleteByCode(code, clazz);
    }

    @Override
    public void enableById(long id) {
        Dew.ds(ds()).enableById(id, clazz);
    }

    @Override
    public void enableByCode(String code) {
        Dew.ds(ds()).enableByCode(code, clazz);
    }

    @Override
    public void disableById(long id) {
        Dew.ds(ds()).disableById(id, clazz);
    }

    @Override
    public void disableByCode(String code) {
        Dew.ds(ds()).disableByCode(code, clazz);
    }

    @Override
    public boolean existById(long id) {
        return Dew.ds(ds()).existById(id, clazz);
    }

    @Override
    public boolean existByCode(String code) {
        return Dew.ds(ds()).existByCode(code, clazz);
    }

    @Override
    public List<E> findAll(LinkedHashMap<String, Boolean> orderDesc) {
        return Dew.ds(ds()).findAll(orderDesc, clazz);
    }

    @Override
    public List<E> findEnabled(LinkedHashMap<String, Boolean> orderDesc) {
        return Dew.ds(ds()).findEnabled(orderDesc, clazz);
    }

    @Override
    public List<E> findDisabled(LinkedHashMap<String, Boolean> orderDesc) {
        return Dew.ds(ds()).findDisabled(orderDesc, clazz);
    }

    @Override
    public long countAll() {
        return Dew.ds(ds()).countAll(clazz);
    }

    @Override
    public long countEnabled() {
        return Dew.ds(ds()).countEnabled(clazz);
    }

    @Override
    public long countDisabled() {
        return Dew.ds(ds()).countDisabled(clazz);
    }

    @Override
    public Page<E> paging(long pageNumber, int pageSize, LinkedHashMap<String, Boolean> orderDesc) {
        return Dew.ds(ds()).paging(pageNumber, pageSize, orderDesc, clazz);
    }

    @Override
    public Page<E> pagingEnabled(int pageNumber, int pageSize, LinkedHashMap<String, Boolean> orderDesc) {
        return Dew.ds(ds()).pagingEnabled(pageNumber, pageSize, orderDesc, clazz);
    }

    @Override
    public Page<E> pagingDisabled(int pageNumber, int pageSize, LinkedHashMap<String, Boolean> orderDesc) {
        return Dew.ds(ds()).pagingDisabled(pageNumber, pageSize, orderDesc, clazz);
    }

}

