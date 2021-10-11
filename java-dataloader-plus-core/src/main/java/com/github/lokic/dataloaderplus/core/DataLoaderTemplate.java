package com.github.lokic.dataloaderplus.core;

import com.github.lokic.javaplus.CompletableFutures;
import org.dataloader.DataLoaderOptions;
import org.dataloader.DataLoaderRegistry;

import java.util.concurrent.CompletableFuture;

public class DataLoaderTemplate {

    private final DataLoaderOptions options;
    private final DataLoaderFactory factory;

    public DataLoaderTemplate(TemplateConfig options) {
        this.options = options.getOptions();
        this.factory = options.getFactory();
    }

    public <R> CompletableFuture<R> using(DataLoaderCallback<CompletableFuture<R>> callback) throws Throwable {
        return using(null, callback);
    }

    public <R> CompletableFuture<R> using(ExDataLoaderRegistry registry, DataLoaderCallback<CompletableFuture<R>> callback) throws Throwable {
        // 虽然options和factory可能与registry中的options和factory不同，但是以最外层的为准，所以复用已经存在的registry。
        if (registry == null) {
            registry = new ExDataLoaderRegistry(options, factory, new DataLoaderRegistry());
        }
        return execute(registry, callback);
    }

    private <R> CompletableFuture<R> execute(ExDataLoaderRegistry registry, DataLoaderCallback<CompletableFuture<R>> callback) throws Throwable {
        boolean isOutermost = false;
        try {
            if (RegistryHolder.getRegistry() == null) {
                isOutermost = true;
                RegistryHolder.setRegistry(registry);
            } else {
                if (RegistryHolder.getRegistry() != registry) {
                    throw new IllegalArgumentException("register must be same");
                }
            }
            CompletableFuture<R> future = callback.doInDataLoader(registry);
            if (isOutermost) {
                RegistryHolder.tryDispatchAll();
                return CompletableFutures.supply(future::join);
            }
            return future;
        } finally {
            // 如果是最外层设置registry的方法，则最后需要清除registry
            if (isOutermost) {
                RegistryHolder.clear();
            }
        }
    }
}