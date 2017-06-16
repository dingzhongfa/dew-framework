package com.tairanchina.csp.dew.core.jdbc;

import com.ecfront.dew.common.Page;
import com.tairanchina.csp.dew.core.Dew;

import java.lang.reflect.ParameterizedType;
import java.util.LinkedHashMap;
import java.util.List;

public abstract class DewDaoImpl<E> implements DewDao<E> {

    private Class<E> clazz = (Class<E>) (((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]);

    @Override
    public long insert(Object entity) {
        return Dew.ds.insert(entity);
    }

    @Override
    public void insert(Iterable<?> entities) {
        Dew.ds.insert(entities);
    }

    @Override
    public void updateById(long id, Object entity) {
        Dew.ds.updateById(id, entity);
    }

    @Override
    public void updateByCode(String code, Object entity) {
        Dew.ds.updateByCode(code, entity);
    }

    @Override
    public E getById(long id) {
        return Dew.ds.getById(id, clazz);
    }

    @Override
    public E getByCode(String code) {
        return Dew.ds.getByCode(code, clazz);
    }

    @Override
    public void deleteById(long id) {
        Dew.ds.deleteById(id, clazz);
    }

    @Override
    public void deleteByCode(String code) {
        Dew.ds.deleteByCode(code, clazz);
    }

    @Override
    public void enableById(long id) {
        Dew.ds.enableById(id, clazz);
    }

    @Override
    public void enableByCode(String code) {
        Dew.ds.enableByCode(code, clazz);
    }

    @Override
    public void disableById(long id) {
        Dew.ds.disableById(id, clazz);
    }

    @Override
    public void disableByCode(String code) {
        Dew.ds.disableByCode(code, clazz);
    }

    @Override
    public boolean existById(long id) {
        return Dew.ds.existById(id, clazz);
    }

    @Override
    public boolean existByCode(String code) {
        return Dew.ds.existByCode(code, clazz);
    }

    @Override
    public List<E> findAll(LinkedHashMap<String, Boolean> orderDesc) {
        return Dew.ds.findAll(orderDesc, clazz);
    }

    @Override
    public List<E> findEnabled(LinkedHashMap<String, Boolean> orderDesc) {
        return Dew.ds.findEnabled(orderDesc, clazz);
    }

    @Override
    public List<E> findDisabled(LinkedHashMap<String, Boolean> orderDesc) {
        return Dew.ds.findDisabled(orderDesc, clazz);
    }

    @Override
    public long countAll() {
        return Dew.ds.countAll(clazz);
    }

    @Override
    public long countEnabled() {
        return Dew.ds.countEnabled(clazz);
    }

    @Override
    public long countDisabled() {
        return Dew.ds.countDisabled(clazz);
    }

    @Override
    public Page<E> paging(long pageNumber, int pageSize, LinkedHashMap<String, Boolean> orderDesc) {
        return Dew.ds.paging(pageNumber, pageSize, orderDesc, clazz);
    }

    @Override
    public Page<E> pagingEnabled(int pageNumber, int pageSize, LinkedHashMap<String, Boolean> orderDesc) {
        return Dew.ds.pagingEnabled(pageNumber, pageSize, orderDesc, clazz);
    }

    @Override
    public Page<E> pagingDisabled(int pageNumber, int pageSize, LinkedHashMap<String, Boolean> orderDesc) {
        return Dew.ds.pagingDisabled(pageNumber, pageSize, orderDesc, clazz);
    }

}

