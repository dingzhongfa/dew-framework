package com.tairanchina.csp.dew.core.autoconfigure;

import org.springframework.cloud.client.SpringCloudApplication;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringCloudApplication
@DewBootApplication
public @interface DewCloudApplication {

}
