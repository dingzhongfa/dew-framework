package com.tairanchina.csp.dew.core;

import com.tairanchina.csp.dew.core.client.ThriftClientAnnotationBeanPostProcessor;
import com.tairanchina.csp.dew.core.client.ThriftPoolAutoConfiguration;
import com.tairanchina.csp.dew.core.client.ThriftProperty;
import com.tairanchina.csp.dew.core.server.ThriftServerAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Created by 迹_Jason on 2017/8/10.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@EnableAutoConfiguration
@Import({ThriftServerAutoConfiguration.class,
ThriftPoolAutoConfiguration.class,ThriftProperty.class,
ThriftClientAnnotationBeanPostProcessor.class})
public @interface EnabledThriftDiscovery {
}
