package com.github.lokic.dataloaderplus.spring;

import com.github.lokic.dataloaderplus.core.ExDataLoaderRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.FactoryBean;

/**
 * 基于 @{@link com.github.lokic.dataloaderplus.core.annotation.DataLoaderService} 注解的接口的实现类工厂
 *
 * @param <T>
 */
@Slf4j
public class DataLoaderServiceFactoryBean<T> implements FactoryBean<T> {

    private final String innerClassName;

    public DataLoaderServiceFactoryBean(String innerClassName) {
        this.innerClassName = innerClassName;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T getObject() throws Exception {
        Class<?> innerClass = Class.forName(innerClassName);
        if (!innerClass.isInterface()) {
            throw new IllegalArgumentException("only support interface");
        }
        return (T) ExDataLoaderRegistry.getService(innerClass);
    }

    @Override
    public Class<?> getObjectType() {
        try {
            return Class.forName(innerClassName);
        } catch (ClassNotFoundException e) {
            log.error("not found class", e);
        }
        return null;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
