package com.tairanchina.csp.dew.core.dao;

import com.ecfront.dew.common.Page;
import com.tairanchina.csp.dew.core.CRUDSTestEntity;
import com.tairanchina.csp.dew.core.jdbc.annotations.ModelParam;
import com.tairanchina.csp.dew.core.jdbc.annotations.Param;
import com.tairanchina.csp.dew.core.jdbc.annotations.Select;

/**
 * Created by 迹_Jason on 2017/7/27.
 */
public interface TestInterfaceDao {

    @Select(value = "select * from t_test_crud_s_entity where field_a= #{ fieldA }", entityClass = CRUDSTestEntity.class)
    Page<CRUDSTestEntity> getYouName(@ModelParam CRUDSTestEntity model, @Param("pageNumber") Long pageNumber, @Param("pageSize") Integer pageSize);
}
