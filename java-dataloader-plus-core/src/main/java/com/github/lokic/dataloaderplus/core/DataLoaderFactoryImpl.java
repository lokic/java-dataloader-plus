package com.github.lokic.dataloaderplus.core;

import lombok.SneakyThrows;

public class DataLoaderFactoryImpl extends AbstractDataLoaderFactory {
    @SneakyThrows
    public <T extends MultiKeyMappedBatchLoader<?, ?>> T createInstance(Class<T> batchLoaderType) {
        return batchLoaderType.getConstructor().newInstance();
    }
}
