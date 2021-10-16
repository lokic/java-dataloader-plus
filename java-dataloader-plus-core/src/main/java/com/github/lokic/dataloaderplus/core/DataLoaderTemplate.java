package com.github.lokic.dataloaderplus.core;

import com.github.lokic.javaplus.CompletableFutures;
import org.dataloader.DataLoaderOptions;
import org.dataloader.DataLoaderRegistry;

import java.util.concurrent.CompletableFuture;

public class DataLoaderTemplate {

    private final DataLoaderOptions options;
    private final DataLoaderFactory factory;
    private final Propagation propagation;

    public DataLoaderTemplate(TemplateConfig options) {
        this.options = options.getOptions();
        this.factory = options.getFactory();
        this.propagation = options.getPropagation();
    }

    public <R> CompletableFuture<R> using(DataLoaderCallback<CompletableFuture<R>> callback) throws Throwable {
        return using(null, callback);
    }

    public <R> CompletableFuture<R> using(ExDataLoaderRegistry registry, DataLoaderCallback<CompletableFuture<R>> callback) throws Throwable {
        ExDataLoaderRegistry newRegistry = prepareRegistry(registry);
        return execute(newRegistry, callback);
    }

    private ExDataLoaderRegistry prepareRegistry(ExDataLoaderRegistry registry) {
        switch (propagation) {
            case REQUIRED:
                if (registry == null) {
                    return newExDataLoaderRegistry();
                } else {
                    // 虽然options和factory可能与registry中的options和factory不同，以最外层的为准，所以复用已经存在的registry。
                    return new ExDataLoaderRegistry(registry);
                }
            case REQUIRES_NEW:
                return newExDataLoaderRegistry();
            default:
                throw new UnsupportedOperationException("unsupported propagation");
        }
    }

    private ExDataLoaderRegistry newExDataLoaderRegistry() {
        return new ExDataLoaderRegistry(options, factory, new DataLoaderRegistry());
    }

    private <R> CompletableFuture<R> execute(ExDataLoaderRegistry registry, DataLoaderCallback<CompletableFuture<R>> callback) throws Throwable {
        RegistryStatus registryStatus = getRegistryStatus(registry);
        try {
            CompletableFuture<R> future = callback.invokeInDataLoader(registryStatus.getRegistry());
            afterInvoke(registryStatus);
            return processResult(registryStatus, future);
        } finally {
            afterCompletion(registryStatus);
        }
    }

    private RegistryStatus getRegistryStatus(ExDataLoaderRegistry registry) {
        ExDataLoaderRegistry suspendedRegistry = null;
        if (RegistryManager.isActive()) {
            if (RegistryManager.getRegistry() == registry) {
                throw new IllegalStateException("registry must be not the same");
            }
            suspendedRegistry = RegistryManager.suspend();
        }
        RegistryManager.init(registry);
        return new RegistryStatus(registry, suspendedRegistry);
    }

    private void afterInvoke(RegistryStatus status) {
        RegistryManager.tryDispatchAll(status.getRegistry());
    }

    private void afterCompletion(RegistryStatus status) {
        if (status.isFirstNest()) {
            // 如果是最外层设置registry的方法，则最后需要清除registry
            RegistryManager.clear();
        } else {
            RegistryManager.resume(status.getSuspendedRegistry());
        }
    }

    private <R> CompletableFuture<R> processResult(RegistryStatus status, CompletableFuture<R> future) {
        if (status.isFirstNest()) {
            return CompletableFutures.supply(future::join);
        }
        return future;
    }

    private static class RegistryStatus {
        private final ExDataLoaderRegistry registry;
        private final ExDataLoaderRegistry suspendedRegistry;

        public RegistryStatus(ExDataLoaderRegistry registry, ExDataLoaderRegistry suspendedRegistry) {
            this.registry = registry;
            this.suspendedRegistry = suspendedRegistry;
        }

        public ExDataLoaderRegistry getRegistry() {
            return registry;
        }

        public ExDataLoaderRegistry getSuspendedRegistry() {
            return suspendedRegistry;
        }

        public boolean isFirstNest() {
            return suspendedRegistry == null;
        }
    }

}