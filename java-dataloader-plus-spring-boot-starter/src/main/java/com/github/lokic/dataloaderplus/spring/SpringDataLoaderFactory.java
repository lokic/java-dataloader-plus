package com.github.lokic.dataloaderplus.spring;

import com.github.lokic.dataloaderplus.core.AbstractDataLoaderFactory;
import com.github.lokic.dataloaderplus.core.MultiKeyMappedBatchLoader;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

public class SpringDataLoaderFactory extends AbstractDataLoaderFactory {

    private final AutowireCapableBeanFactory beanFactory;

    public SpringDataLoaderFactory(AutowireCapableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public <T extends MultiKeyMappedBatchLoader<?, ?>> T createInstance(Class<T> batchLoaderType) {
        return beanFactory.createBean(batchLoaderType);
    }

}
