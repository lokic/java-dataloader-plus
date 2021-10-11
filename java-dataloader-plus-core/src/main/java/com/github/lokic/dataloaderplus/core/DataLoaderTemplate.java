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
        ExDataLoaderRegistry registry = RegistryHolder.getRegistry();
        if (registry == null || !isEqualConfig(registry)) {
            registry = new ExDataLoaderRegistry(options, factory,
                    registry == null ? new DataLoaderRegistry() : registry.getRegistry());
        }
        return execute(registry, callback);
    }

    private boolean isEqualConfig(ExDataLoaderRegistry registry) {
        return options == registry.getOptions() && factory == registry.getFactory();
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