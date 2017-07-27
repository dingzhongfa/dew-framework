package com.tairanchina.csp.dew.core.dao;

import com.ecfront.dew.common.Page;
import com.tairanchina.csp.dew.core.CRUDSTestEntity;
import com.tairanchina.csp.dew.core.jdbc.DewDao;
import com.tairanchina.csp.dew.core.jdbc.annotations.ModelParam;
import com.tairanchina.csp.dew.core.jdbc.annotations.Param;
import com.tairanchina.csp.dew.core.jdbc.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by 迹_Jason on 2017/7/27.
 */
public interface TestInterfaceDao extends DewDao<CRUDSTestEntity>{

    @Select(value = "select * from t_test_crud_s_entity where field_a= #{ fieldA }", entityClass = CRUDSTestEntity.class)
    Page<CRUDSTestEntity> queryByCustomPaging(@ModelParam CRUDSTestEntity model, @Param("pageNumber") Long pageNumber, @Param("pageSize") Integer pageSize);

    @Select(value = "select * from t_test_crud_s_entity where field_a= #{ fieldA }", entityClass = CRUDSTestEntity.class)
    Page<CRUDSTestEntity> queryByDefaultPaging(@ModelParam CRUDSTestEntity model);

    @Select(value = "select * from t_test_crud_s_entity where field_a= #{ fieldA }", entityClass = CRUDSTestEntity.class)
    List<CRUDSTestEntity> queryList(@ModelParam CRUDSTestEntity model);

    @Select(value = "select * from t_test_crud_s_entity where field_a= #{ fieldA }", entityClass = CRUDSTestEntity.class)
    List<CRUDSTestEntity> queryByField(@Param("fieldA") String fieldA);

    @Select(value = "select * from t_test_crud_s_entity where field_a= #{ fieldA } and field_c = #{fc}", entityClass = CRUDSTestEntity.class)
    List<CRUDSTestEntity> queryByTowFields(@Param("fieldA") String fieldA, @Param("fc") String f);

    @Select(value = "select * from t_test_crud_s_entity where id= #{id}", entityClass = CRUDSTestEntity.class)
    CRUDSTestEntity getById(@Param("id") long id);

    @Select(value = "select * from t_test_crud_s_entity where id= #{id}")
    Map<String,Object> getMapById(@Param("id") long id);

}
