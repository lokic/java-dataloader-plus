package me.lokic.dataloaderplus.core;

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
        DATA_LOADER_CREATORS.put(dataLoaderProvider.getClass().getName(), dataLoaderProvider);
    }

    public DataLoader<?, ?> create(String name, DataLoaderOptions options) {
        MultiKeyMappedBatchLoader<?, ?> loader = Optional.ofNullable(DATA_LOADER_CREATORS.get(name))
                .orElseThrow(() -> new IllegalArgumentException("not found data loader supplier"));

        return DataLoader.newMappedDataLoader(loader, options);
    }

}
