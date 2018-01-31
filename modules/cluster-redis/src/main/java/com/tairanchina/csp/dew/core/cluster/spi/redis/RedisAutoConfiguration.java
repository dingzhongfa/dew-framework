package com.tairanchina.csp.dew.core.cluster.spi.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * desription:
 * Created by ding on 2018/1/25.
 */
@Configuration
public class RedisAutoConfiguration {

    @Bean
    @ConditionalOnExpression("'${dew.cluster.cache}'=='redis' ")
    public RedisClusterCache redisClusterCache(RedisTemplate<String, String> redisTemplate) {
        return new RedisClusterCache(redisTemplate);
    }

    @Bean
    @ConditionalOnExpression("'${dew.cluster.dist}'=='redis'")
    public RedisClusterDist redisClusterDist(@Autowired RedisTemplate<String, String> redisTemplate) {
        return new RedisClusterDist(redisTemplate);
    }

    @Bean
    @ConditionalOnExpression("'${dew.cluster.mq}'=='redis'")
    public RedisClusterMQ redisClusterMQ(@Autowired RedisTemplate<String, String> redisTemplate) {
        return new RedisClusterMQ(redisTemplate);
    }


}
