package com.github.lokic.dataloaderplus.spring;

import com.github.lokic.custom.registrar.ProxyFactoryBean;
import com.github.lokic.custom.registrar.ProxyRegistrar;
import com.github.lokic.dataloaderplus.core.annotation.DataLoaderService;
import com.github.lokic.dataloaderplus.spring.annotation.EnableDataLoader;

import java.lang.annotation.Annotation;

public class DataLoaderRegistrar extends ProxyRegistrar {

    @Override
    protected Class<? extends Annotation> getEnableAnnotationType() {
        return EnableDataLoader.class;
    }

    @Override
    protected Class<? extends Annotation> getClassAnnotationType() {
        return DataLoaderService.class;
    }

    @Override
    protected Class<? extends ProxyFactoryBean> getFactoryBeanType() {
        return DataLoaderServiceFactory.class;
    }

}

