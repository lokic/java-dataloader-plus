package com.github.lokic.dataloaderplus.core;

import lombok.SneakyThrows;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderOptions;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractDataLoaderFactory implements  DataLoaderFactory {

    private final Map<Class<? extends MultiKeyMappedBatchLoader<?, ?>>, MultiKeyMappedBatchLoader<?, ?>> dataLoaderCreators = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    @Override
    public <T extends MultiKeyMappedBatchLoader<?, ?>> T getInstance(Class<T> batchLoaderType) {
        return (T) dataLoaderCreators.computeIfAbsent(batchLoaderType, this::createInstance);
    }

    public abstract <T extends MultiKeyMappedBatchLoader<?, ?>> T createInstance(Class<T> batchLoaderType);

    @SneakyThrows
    @Override
    public DataLoader<?, ?> create(Class<? extends MultiKeyMappedBatchLoader<?, ?>> batchLoaderType, DataLoaderOptions options) {
        MultiKeyMappedBatchLoader<?, ?> loader = getInstance(batchLoaderType);
        return org.dataloader.DataLoaderFactory.newMappedDataLoader(loader, options);
    }

}
