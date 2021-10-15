package com.github.lokic.dataloaderplus.core;

import org.dataloader.DataLoader;
import org.dataloader.DataLoaderOptions;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * DataLoader的工厂，基于 {@link MultiKeyMappedBatchLoader} 创建 {@link DataLoader}
 */
public class DataLoaderFactory {

    private final Map<String, MultiKeyMappedBatchLoader<?, ?>> DATA_LOADER_CREATORS = new ConcurrentHashMap<>();

    public void addMultiKeyMappedBatchLoader(MultiKeyMappedBatchLoader<?, ?> dataLoaderProvider) {
        addMultiKeyMappedBatchLoader(dataLoaderProvider.getClass().getName(), dataLoaderProvider);
    }

    public void addMultiKeyMappedBatchLoader(String name, MultiKeyMappedBatchLoader<?, ?> dataLoaderProvider) {
        DATA_LOADER_CREATORS.put(name, dataLoaderProvider);
    }

    public MultiKeyMappedBatchLoader<?, ?> getMultiKeyMappedBatchLoader(String name) {
        return DATA_LOADER_CREATORS.get(name);
    }

    public DataLoader<?, ?> create(String name, DataLoaderOptions options) {
        MultiKeyMappedBatchLoader<?, ?> loader = Optional.ofNullable(DATA_LOADER_CREATORS.get(name))
                .orElseThrow(() -> new IllegalArgumentException("not found data loader supplier, name = " + name));

        return org.dataloader.DataLoaderFactory.newMappedDataLoader(loader, options);
    }

}
