package com.tairanchina.csp.dew.core.controller;

/**
 * VO组装器
 *
 * @param <V>
 * @param <E>
 */
public interface VOAssembler<V, E> {

    default V entityToVO(E entity) throws RuntimeException {
        return (V) entity;
    }

    default E voToEntity(V vo) throws RuntimeException {
        return (E) vo;
    }

}
