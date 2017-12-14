package com.tairanchina.csp.dew.jdbc.sharding.annotion;

import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.AdviceModeImportSelector;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.core.type.AnnotationMetadata;

/**
 * desription:
 * Created by ding on 2017/12/13.
 */
public class ShardingSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        Class<?> annoType = GenericTypeResolver.resolveTypeArgument(getClass(), AdviceModeImportSelector.class);
        AnnotationAttributes attributes = attributesFor(importingClassMetadata, annoType.getName());
        if (attributes == null) {
            throw new IllegalArgumentException(String.format(
                    "@%s is not present on importing class '%s' as expected",
                    annoType.getSimpleName(), importingClassMetadata.getClassName()));
        }

        AdviceMode adviceMode = attributes.getEnum("");
        String[] imports = selectImports();
        if (imports == null) {
            throw new IllegalArgumentException(String.format("Unknown AdviceMode: '%s'", adviceMode));
        }
        return imports;
    }
    private AnnotationAttributes attributesFor(AnnotatedTypeMetadata metadata, String annotationClassName) {
        return AnnotationAttributes.fromMap(metadata.getAnnotationAttributes(annotationClassName, false));
    }
}
