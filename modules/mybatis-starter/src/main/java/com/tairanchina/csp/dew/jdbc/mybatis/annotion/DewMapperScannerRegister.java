package com.tairanchina.csp.dew.jdbc.mybatis.annotion;

import org.mybatis.spring.mapper.ClassPathMapperScanner;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * desription:
 * Created by ding on 2017/12/28.
 */
public class DewMapperScannerRegister implements ImportBeanDefinitionRegistrar, ResourceLoaderAware {

    private static final Logger logger = LoggerFactory.getLogger(DewMapperScannerRegister.class);

    public static String secondPrefix = "";

    private ResourceLoader resourceLoader;

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        // 获取DewMapperScanner字段值
        AnnotationAttributes annoAttrs = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(DewMapperScan.class.getName()));
        Map<Class<? extends Annotation>, ClassPathMapperScanner> scanners = new HashMap<Class<? extends Annotation>, ClassPathMapperScanner>() {{
            put(Primary.class, new ClassPathMapperScanner(registry));
            put(Second.class, new ClassPathMapperScanner(registry));
            if (annoAttrs.getBoolean("enableSharding")) {
                logger.info("use sharding-jdbc with mybatis");
                put(Sharding.class, new ClassPathMapperScanner(registry));
            }
        }};
        scanners.forEach((k, v) -> scan(k, v, annoAttrs));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    private void scan(Class<? extends Annotation> annotationClass, ClassPathMapperScanner scanner, AnnotationAttributes annoAttrs) {
        if (resourceLoader != null) {
            scanner.setResourceLoader(resourceLoader);
        }
        if (!Annotation.class.equals(annotationClass)) {
            scanner.setAnnotationClass(annotationClass);
        }
        Class<? extends MapperFactoryBean> mapperFactoryBeanClass = annoAttrs.getClass("factoryBean");
        if (!MapperFactoryBean.class.equals(mapperFactoryBeanClass)) {
            scanner.setMapperFactoryBean(BeanUtils.instantiateClass(mapperFactoryBeanClass));
        }
        if (annotationClass.equals(Primary.class)) {
            scanner.setSqlSessionTemplateBeanName("sqlSessionTemplate");
        }
        if (annotationClass.equals(Second.class)) {
            secondPrefix = annoAttrs.getString("secondPrefix");
            if (StringUtils.isEmpty(secondPrefix)) {
                logger.info("no spercific secondSqlSessionTemplate, use primarySqlSessionTemplate instead");
            }
            scanner.setSqlSessionTemplateBeanName("secondSqlSessionTemplate");
        }
        if (annotationClass.equals(Sharding.class)) {
            scanner.setSqlSessionTemplateBeanName("shardingSqlSessionTemplate");
        }
        List<String> basePackages = new ArrayList<String>();
        for (String pkg : annoAttrs.getStringArray("value")) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }
        for (String pkg : annoAttrs.getStringArray("basePackages")) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }
        for (Class<?> clazz : annoAttrs.getClassArray("basePackageClasses")) {
            basePackages.add(ClassUtils.getPackageName(clazz));
        }
        scanner.registerFilters();
        scanner.doScan(StringUtils.toStringArray(basePackages));
    }

}
