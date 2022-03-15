package com.github.lokic.dataloaderplus.spring;

import com.github.lokic.custom.registrar.InterfaceFactory;
import com.github.lokic.custom.registrar.InterfaceRegistrar;
import com.github.lokic.dataloaderplus.core.annotation.DataLoaderService;
import com.github.lokic.dataloaderplus.spring.annotation.EnableDataLoader;

import java.lang.annotation.Annotation;

public class DataLoaderRegistrar extends InterfaceRegistrar {

    @Override
    protected Class<? extends Annotation> getEnableAnnotationType() {
        return EnableDataLoader.class;
    }

    @Override
    protected Class<? extends Annotation> getAnnotationType() {
        return DataLoaderService.class;
    }

    @Override
    protected Class<? extends InterfaceFactory> getFactoryBeanType() {
        return DataLoaderServiceFactory.class;
    }

}

