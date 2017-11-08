package com.tairanchina.csp.dew.config.configuration;

/*
import com.tairanchina.csp.dew.config.refresh.DewContextRefresher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.cloud.autoconfigure.RefreshEndpointAutoConfiguration;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.cloud.context.scope.refresh.RefreshScope;
import org.springframework.cloud.endpoint.RefreshEndpoint;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

*/
/**
 * desription:
 * Created by ding on 2017/11/8.
 *//*

@Configuration
@AutoConfigureBefore(value ={RefreshAutoConfiguration.class,RefreshEndpointAutoConfiguration.class})
public class DewRefreshConfiguration {

    @Bean
    public DewContextRefresher contextRefresher(ConfigurableApplicationContext context,
                                             RefreshScope scope) {
        return new DewContextRefresher(context, scope);
    }

    @Bean
    public RefreshEndpoint refreshEndpoint(@Autowired DewContextRefresher contextRefresher) {
        RefreshEndpoint endpoint = new RefreshEndpoint(contextRefresher);
        return endpoint;
    }
}
*/
