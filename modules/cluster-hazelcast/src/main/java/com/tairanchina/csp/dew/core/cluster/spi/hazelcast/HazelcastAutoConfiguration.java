package com.tairanchina.csp.dew.core.cluster.spi.hazelcast;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * desription:
 * Created by ding on 2018/1/25.
 */
@Configuration
@EnableConfigurationProperties(HazelcastConfig.class)
public class HazelcastAutoConfiguration {

    private HazelcastConfig hazelcastConfig;

    public HazelcastAutoConfiguration(HazelcastConfig hazelcastConfig) {
        this.hazelcastConfig = hazelcastConfig;
    }

    @Bean
    @ConditionalOnExpression("#{'${dew.cluster.cache}'=='hazelcast' || '${dew.cluster.mq}'=='hazelcast' || '${dew.cluster.dist}'=='hazelcast'}")
    public HazelcastAdapter hazelcastAdapter(){
        return new HazelcastAdapter(hazelcastConfig);
    }

    @Bean
    @ConditionalOnExpression("'${dew.cluster.dist}'=='hazelcast'")
    public HazelcastClusterDist hazelcastClusterDist(HazelcastAdapter hazelcastAdapter){
        return  new HazelcastClusterDist(hazelcastAdapter);
    }

    @Bean
    @ConditionalOnExpression("'${dew.cluster.mq}'=='hazelcast'")
    public HazelcastClusterMQ hazelcastClusterMQ(HazelcastAdapter hazelcastAdapter){
        return new HazelcastClusterMQ(hazelcastAdapter);
    }
}
