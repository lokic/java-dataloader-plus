package com.github.lokic.dataloaderplus.core;

import com.github.lokic.dataloaderplus.core.kits.CompletableFutures;
import org.dataloader.DataLoaderOptions;

import java.util.concurrent.CompletableFuture;

public class DataLoaderTemplate {

    private final DataLoaderOptions options;
    private final DataLoaderFactory factory;

    private static final RegisterCallback NONE = registry -> {
    };

    public DataLoaderTemplate(TemplateConfig options) {
        this.options = options.getOptions();
        this.factory = options.getFactory();
    }

    public <R> R using(RegistryFactory registryFactory, DataLoaderCallback<CompletableFuture<R>> callback) {
        return using(registryFactory, NONE, callback);
    }

    public <R> R using(RegisterCallback registerCallback, DataLoaderCallback<CompletableFuture<R>> callback) {
        return using(RegistryFactoryType.DEFAULT, registerCallback, callback);
    }

    public <R> R using(RegistryFactory registryFactory, RegisterCallback registerCallback, DataLoaderCallback<CompletableFuture<R>> callback) {
        ExDataLoaderRegistry registry = new ExDataLoaderRegistry(options, factory, registryFactory.newRegistry());
        registerCallback.doInRegister(registry);
        return using(registry, callback);
    }

    public <R> R using(ExDataLoaderRegistry registry, DataLoaderCallback<CompletableFuture<R>> callback) {
        return execute(registry, reg -> CompletableFutures.join(execute(reg, callback)));
    }

    private <R> R execute(ExDataLoaderRegistry registry, DataLoaderCallback<R> callback) {
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
            return callback.doInDataLoader(registry);
        } finally {
            // 如果是最外层设置registry的方法，则最后需要清除registry
            if (isOutermost) {
                RegistryHolder.clear();
            }
        }
    }
}