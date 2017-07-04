package com.tairanchina.csp.dew.storage;


import com.tairanchina.csp.dew.model.Account;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * Created by panshuai on 17/6/29.
 */
@Mapper
public interface AccountMapper {

    @Select("select id, user_name userName, age, amount, version from account where id = #{id}")
    Account getAccount(@Param("id") Integer id);

    @Update("update account set amount = #{amount}, version = #{newVersion} where id = #{id} and version = #{oldVersion}")
    int updateAmount(@Param("id") Integer userId,
                  @Param("amount") Integer amount,
                  @Param("newVersion") Long newVersion,
                  @Param("oldVersion") Long oldVersion);
}
