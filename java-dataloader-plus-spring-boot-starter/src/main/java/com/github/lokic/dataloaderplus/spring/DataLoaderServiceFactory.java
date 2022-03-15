package com.github.lokic.dataloaderplus.spring;

import com.github.lokic.custom.registrar.InterfaceFactory;
import com.github.lokic.dataloaderplus.core.ExDataLoaderRegistry;
import lombok.NonNull;

public class DataLoaderServiceFactory extends InterfaceFactory {

    public DataLoaderServiceFactory(@NonNull Class<?> innerClass) {
        super(innerClass);
    }

    @Override
    public Object getObject(Class<?> clazz) {
        return ExDataLoaderRegistry.getService(clazz);
    }
}
