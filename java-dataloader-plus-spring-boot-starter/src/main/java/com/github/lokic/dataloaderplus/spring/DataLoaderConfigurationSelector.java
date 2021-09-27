package com.github.lokic.dataloaderplus.spring;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

public class DataLoaderConfigurationSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[]{DataLoaderRegistrar.class.getName(), DataLoaderAutoConfiguration.class.getName()};
    }
}
