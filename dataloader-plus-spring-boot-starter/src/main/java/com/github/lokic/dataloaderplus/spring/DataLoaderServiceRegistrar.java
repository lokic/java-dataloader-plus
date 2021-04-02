package com.github.lokic.dataloaderplus.spring;

import com.github.lokic.dataloaderplus.core.kits.Types;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;

import java.util.HashSet;
import java.util.Set;

public class DataLoaderServiceRegistrar implements ResourceLoaderAware, ImportBeanDefinitionRegistrar {

    private ResourceLoader resourceLoader;

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        Set<String> basePackages = getBasePackages(importingClassMetadata);

        DataLoaderServiceScanner scanner = new DataLoaderServiceScanner(registry);
        scanner.setResourceLoader(this.resourceLoader);
        scanner.doScan(basePackages.toArray(new String[0]));
    }

    /**
     * 生成basePackages
     *
     * @param importingClassMetadata
     * @return
     */
    Set<String> getBasePackages(AnnotationMetadata importingClassMetadata) {
        Set<String> basePackages = new HashSet<>();
        basePackages.add(
                Types.getPackageName(importingClassMetadata.getClassName()));

        return basePackages;
    }

}

