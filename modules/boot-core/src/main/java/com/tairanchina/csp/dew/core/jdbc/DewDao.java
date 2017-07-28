package com.tairanchina.csp.dew.core.jdbc;

import com.ecfront.dew.common.Page;
import com.tairanchina.csp.dew.core.Dew;
import org.springframework.cglib.proxy.Proxy;

import java.lang.reflect.ParameterizedType;
import java.util.LinkedHashMap;
import java.util.List;

public interface DewDao<E> {

    default Class<E> getClazz() {
        if (Proxy.class.isAssignableFrom(this.getClass())) {
            return (Class<E>) (((ParameterizedType) this.getClass().getInterfaces()[0].getGenericInterfaces()[0]).getActualTypeArguments()[0]);
        } else {
            return (Class<E>) (((ParameterizedType) this.getClass().getGenericInterfaces()[0]).getActualTypeArguments()[0]);
        }
    }

    default String ds() {
        return "";
    }

    default long insert(Object entity) {
        return Dew.ds(ds()).insert(entity);
    }

    default void insert(Iterable<?> entities) {
        Dew.ds(ds()).insert(entities);
    }

    default void updateById(long id, Object entity) {
        Dew.ds(ds()).updateById(id, entity);
    }

    default void updateByCode(String code, Object entity) {
        Dew.ds(ds()).updateByCode(code, entity);
    }

    default E getById(long id) {
        return Dew.ds(ds()).getById(id, getClazz());
    }

    default E getByCode(String code) {
        return Dew.ds(ds()).getByCode(code, getClazz());
    }

    default void deleteById(long id) {
        Dew.ds(ds()).deleteById(id, getClazz());
    }

    default void deleteByCode(String code) {
        Dew.ds(ds()).deleteByCode(code, getClazz());
    }

    default void enableById(long id) {
        Dew.ds(ds()).enableById(id, getClazz());
    }

    default void enableByCode(String code) {
        Dew.ds(ds()).enableByCode(code, getClazz());
    }

    default void disableById(long id) {
        Dew.ds(ds()).disableById(id, getClazz());
    }

    default void disableByCode(String code) {
        Dew.ds(ds()).disableByCode(code, getClazz());
    }

    default boolean existById(long id) {
        return Dew.ds(ds()).existById(id, getClazz());
    }

    default boolean existByCode(String code) {
        return Dew.ds(ds()).existByCode(code, getClazz());
    }

    default List<E> findAll() {
        return findAll(null);
    }

    default List<E> findAll(LinkedHashMap<String, Boolean> orderDesc) {
        return Dew.ds(ds()).findAll(orderDesc, getClazz());
    }

    default List<E> findEnabled() {
        return findEnabled(null);
    }

    default List<E> findEnabled(LinkedHashMap<String, Boolean> orderDesc) {
        return Dew.ds(ds()).findEnabled(orderDesc, getClazz());
    }

    default List<E> findDisabled() {
        return findDisabled(null);
    }

    default List<E> findDisabled(LinkedHashMap<String, Boolean> orderDesc) {
        return Dew.ds(ds()).findDisabled(orderDesc, getClazz());
    }

    default long countAll() {
        return Dew.ds(ds()).countAll(getClazz());
    }

    default long countEnabled() {
        return Dew.ds(ds()).countEnabled(getClazz());
    }

    default long countDisabled() {
        return Dew.ds(ds()).countDisabled(getClazz());
    }

    default Page<E> paging(long pageNumber, int pageSize) {
        return paging(pageNumber, pageSize, null);
    }

    default Page<E> paging(long pageNumber, int pageSize, LinkedHashMap<String, Boolean> orderDesc) {
        return Dew.ds(ds()).paging(pageNumber, pageSize, orderDesc, getClazz());
    }

    default Page<E> pagingEnabled(int pageNumber, int pageSize) {
        return pagingEnabled(pageNumber, pageSize, null);
    }

    default Page<E> pagingEnabled(int pageNumber, int pageSize, LinkedHashMap<String, Boolean> orderDesc) {
        return Dew.ds(ds()).pagingEnabled(pageNumber, pageSize, orderDesc, getClazz());
    }

    default Page<E> pagingDisabled(int pageNumber, int pageSize) {
        return pagingDisabled(pageNumber, pageSize, null);
    }

    default Page<E> pagingDisabled(int pageNumber, int pageSize, LinkedHashMap<String, Boolean> orderDesc) {
        return Dew.ds(ds()).pagingDisabled(pageNumber, pageSize, orderDesc, getClazz());
    }

}

