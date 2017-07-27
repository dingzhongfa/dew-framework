package com.tairanchina.csp.dew.core.jdbc;

import com.tairanchina.csp.dew.core.Dew;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;

/**
 * Created by 迹_Jason on 2017/7/26.
 * 配置初始化
 */
@Component
public class DaoScannerConfigurer {

    @PostConstruct
    public void init(){
        ClassPathScanner scanner = new ClassPathScanner((BeanDefinitionRegistry) ( (GenericApplicationContext) Dew.applicationContext).getBeanFactory());
        scanner.setResourceLoader(Dew.applicationContext);
        scanner.registerFilters();
        scanner.scan(StringUtils.tokenizeToStringArray(Dew.dewConfig.getDao().getBasePackage(), ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS));
    }


}
