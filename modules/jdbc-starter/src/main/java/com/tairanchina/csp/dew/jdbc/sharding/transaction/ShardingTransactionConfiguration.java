package com.tairanchina.csp.dew.jdbc.sharding.transaction;

import com.tairanchina.csp.dew.jdbc.sharding.ShardingEnvironmentAware;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;

/**
 * desription:
 * Created by ding on 2017/12/13.
 */
@Configuration
@ConditionalOnBean(ShardingEnvironmentAware.class)
public class ShardingTransactionConfiguration {
}
