package com.tairanchina.csp.dew.core.test.dataaccess.select.dao;

import com.tairanchina.csp.dew.core.jdbc.DewDao;
import com.tairanchina.csp.dew.core.jdbc.annotations.Param;
import com.tairanchina.csp.dew.core.jdbc.annotations.Select;
import com.tairanchina.csp.dew.core.test.dataaccess.select.entity.SystemConfig;

import java.util.Map;


public interface SystemConfigDao extends DewDao<String ,SystemConfig> {

    @Select("SELECT s.*,t.*" +
            "FROM system_config s " +
            "INNER JOIN test_select_entity t ON s.create_time = t.create_time " +
            "WHERE s.value = #{value}")
    Map testLink(@Param("value") String value);
}
