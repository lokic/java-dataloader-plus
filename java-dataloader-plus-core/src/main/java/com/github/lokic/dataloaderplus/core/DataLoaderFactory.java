package com.github.lokic.dataloaderplus.core;

import lombok.SneakyThrows;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderOptions;

/**
 * DataLoader的工厂，基于 {@link MultiKeyMappedBatchLoader} 创建 {@link DataLoader}
 */
public interface DataLoaderFactory {

    <T extends MultiKeyMappedBatchLoader<?, ?>> T getInstance(Class<T> batchLoaderType);


    @SneakyThrows
    default DataLoader<?, ?> create(Class<? extends MultiKeyMappedBatchLoader<?, ?>> batchLoaderType, DataLoaderOptions options) {
        MultiKeyMappedBatchLoader<?, ?> loader = getInstance(batchLoaderType);
        return org.dataloader.DataLoaderFactory.newMappedDataLoader(loader, options);
    }
}
