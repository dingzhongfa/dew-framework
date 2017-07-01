package com.tairanchina.csp.dew.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tairanchina.csp.dew.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by panshuai on 17/6/26.
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("select age from user group by age")
    public List<String> ageGroup();
}
