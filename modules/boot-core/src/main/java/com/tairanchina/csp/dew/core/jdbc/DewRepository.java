package com.tairanchina.csp.dew.core.jdbc;

import com.ecfront.dew.common.Page;
import com.tairanchina.csp.dew.core.entity.IdEntity;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DewRepository<E extends IdEntity> {


    @Transactional
    <S extends E> List<S> save(Iterable<S> entities);

    @Transactional
    <S extends E> S save(S entity);

    @Transactional
    E updateById(long id, E entity);

    @Transactional
    E updateByCode(String code, E entity);

    E getById(long id);

    E getByCode(String code);

    @Transactional
    void deleteById(long id);

    @Transactional
    void deleteByCode(String code);

    List<E> findAll();

    List<E> findEnable();

    List<E> findDisable();

    Page<E> paging(int pageNumber, int pageSize, Sort sort);

    Page<E> pagingEnable(int pageNumber, int pageSize, Sort sort);

    Page<E> pagingDisable(int pageNumber, int pageSize, Sort sort);

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

    default <E> Page<E> pageConvert(org.springframework.data.domain.Page<E> page) {
        return Page.build(page.getNumber(), page.getNumberOfElements(), page.getTotalElements(), page.getContent());
    }

}

