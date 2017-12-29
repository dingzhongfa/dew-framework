package com.tairanchina.csp.dew.mybatis.multi.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tairanchina.csp.dew.jdbc.mybatis.annotion.Sharding;
import com.tairanchina.csp.dew.mybatis.multi.entity.TOrder;

/**
 * desription:
 * Created by ding on 2017/12/28.
 */
@Sharding
public interface TOrderMapper extends BaseMapper<TOrder> {
}
