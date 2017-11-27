package com.tairanchina.csp.dew.core.autoconfigure;

import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringCloudApplication
@DewBootApplication
public @interface DewCloudApplication {

    @AliasFor(annotation = DewBootApplication.class, attribute = "exclude")
    Class<?>[] exclude() default {};

    @AliasFor(annotation = DewBootApplication.class, attribute = "scanBasePackages")
    String[] scanBasePackages() default {};

    @AliasFor(annotation = DewBootApplication.class, attribute = "scanBasePackageClasses")
    Class<?>[] scanBasePackageClasses() default {};

}
