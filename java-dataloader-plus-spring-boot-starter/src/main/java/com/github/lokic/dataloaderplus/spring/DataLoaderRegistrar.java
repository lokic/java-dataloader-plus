package com.github.lokic.dataloaderplus.spring;

import com.github.lokic.dataloaderplus.core.MultiKeyMappedBatchLoader;
import com.github.lokic.dataloaderplus.core.annotation.DataLoaderMapping;
import com.github.lokic.dataloaderplus.core.kits.Types;
import com.github.lokic.dataloaderplus.spring.annotation.EnableDataLoader;
import lombok.SneakyThrows;
import org.springframework.aop.config.AopConfigUtils;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;

import java.lang.reflect.Method;
import java.util.*;

public class DataLoaderRegistrar implements ResourceLoaderAware, ImportBeanDefinitionRegistrar {

    private ResourceLoader resourceLoader;

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }


    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        // enable proxyTargetClass
        AopConfigUtils.registerAspectJAnnotationAutoProxyCreatorIfNecessary(registry);

        Set<String> basePackages = getBasePackages(importingClassMetadata);

        DataLoaderServiceScanner scanner = new DataLoaderServiceScanner(registry);
        scanner.setResourceLoader(this.resourceLoader);
        scanner.doScan(basePackages.toArray(new String[0]));

        List<Class<? extends MultiKeyMappedBatchLoader<?, ?>>> batchLoaderClasses = parseBatchLoaderClasses(scanner, registry, basePackages);
        for (Class<? extends MultiKeyMappedBatchLoader<?, ?>> batchLoaderClass : batchLoaderClasses) {
            registerToSpring(registry, batchLoaderClass);
        }

    }

    @SneakyThrows
    private List<Class<? extends MultiKeyMappedBatchLoader<?, ?>>> parseBatchLoaderClasses(DataLoaderServiceScanner scanner, BeanDefinitionRegistry registry, Set<String> basePackages) {
        List<Class<? extends MultiKeyMappedBatchLoader<?, ?>>> batchLoaderClasses = new ArrayList<>();
        for (String basePackage : basePackages) {
            Set<BeanDefinition> candidateComponents = scanner.findCandidateComponents(basePackage);
            for (BeanDefinition candidateComponent : candidateComponents) {
                if (candidateComponent instanceof AnnotatedBeanDefinition) {
                    AnnotatedBeanDefinition beanDefinition = (AnnotatedBeanDefinition) candidateComponent;
                    Method[] methods = Class.forName(beanDefinition.getBeanClassName()).getDeclaredMethods();
                    for (Method method : methods) {
                        DataLoaderMapping dataLoaderMapping = AnnotationUtils.findAnnotation(method, DataLoaderMapping.class);
                        if (dataLoaderMapping != null) {
                            Class<? extends MultiKeyMappedBatchLoader<?, ?>> batchLoaderClass = dataLoaderMapping.batchLoader();
                            batchLoaderClasses.add(batchLoaderClass);
                        }
                    }
                }
            }
        }
        return batchLoaderClasses;
    }


    private <T> void registerToSpring(BeanDefinitionRegistry registry, Class<T> clazz) {
        RootBeanDefinition beanDefinition = new RootBeanDefinition();
        beanDefinition.setBeanClass(clazz);
        beanDefinition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_CONSTRUCTOR);
        beanDefinition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
        beanDefinition.setPrimary(true);
        // 此处必须是原始class的名字，后面字节码增强之后，实现的类名会改变
        registry.registerBeanDefinition(clazz.getName(), beanDefinition);
    }

    /**
     * 生成basePackages
     *
     * @param importingClassMetadata
     * @return
     */
    private Set<String> getBasePackages(AnnotationMetadata importingClassMetadata) {
        Set<String> basePackages = new HashSet<>();
        Map<String, Object> annotationAttributes = importingClassMetadata.getAnnotationAttributes(EnableDataLoader.class.getName());
        String[] basePackagesAttr = (String[]) annotationAttributes.get("basePackages");
        if (basePackagesAttr == null || basePackagesAttr.length == 0) {
            basePackages.add(
                    Types.getPackageName(importingClassMetadata.getClassName()));
        } else {
            basePackages.addAll(Arrays.asList(basePackagesAttr));
        }
        return basePackages;
    }

}

