package com.tairanchina.csp.dew.core.dao;

import com.tairanchina.csp.dew.core.jdbc.DewDao;
import com.tairanchina.csp.dew.core.jdbc.annotations.Param;
import com.tairanchina.csp.dew.core.jdbc.annotations.Select;

import java.util.Map;

/**
 * Created by ding on 2017/9/7.
 */
public interface SystemConfigDao extends DewDao<String ,SystemConfig> {

    @Select("SELECT s.*,t.*" +
            "FROM system_config s " +
            "INNER JOIN test_select_entity t ON s.create_time = t.create_time " +
            "WHERE s.value = #{value}")
    Map testLink(@Param("value") String value);
}
