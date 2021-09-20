package com.github.lokic.dataloaderplus.spring;

import com.github.lokic.dataloaderplus.core.ExDataLoaderRegistry;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.FactoryBean;

/**
 * 基于 @{@link com.github.lokic.dataloaderplus.core.annotation.DataLoaderService} 注解的接口的实现类工厂
 *
 * @param <T>
 */
@Slf4j
public class DataLoaderServiceFactoryBean<T> implements FactoryBean<T> {

    private final Class<?> innerClass;

    public DataLoaderServiceFactoryBean(Class<?> innerClass) {
        this.innerClass = innerClass;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T getObject() throws Exception {
        if (!innerClass.isInterface()) {
            throw new IllegalArgumentException("only support interface");
        }
        return (T) ExDataLoaderRegistry.getService(innerClass);
    }

    @SneakyThrows
    @Override
    public Class<?> getObjectType() {
        return innerClass;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
