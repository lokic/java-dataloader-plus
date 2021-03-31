package me.lokic.dataloaderplus.core;

import me.lokic.dataloaderplus.core.kits.CompletableFutures;
import org.dataloader.DataLoaderOptions;

import java.util.concurrent.CompletableFuture;

public class DataLoaderTemplate {

    private final DataLoaderOptions options;
    private final DataLoaderFactory factory;


    public DataLoaderTemplate(TemplateOptions options) {
        this.options = options.getOptions();
        this.factory = options.getFactory();
    }


    public <R> R using(DataLoaderExecutor<CompletableFuture<R>> executor) {
        ExDataLoaderRegistry registry = new ExDataLoaderRegistry(options, factory);
        return execute(registry, reg -> CompletableFutures.join(using(reg, executor)));
    }


    public <R> CompletableFuture<R> using(ExDataLoaderRegistry registry, DataLoaderExecutor<CompletableFuture<R>> executor) {
        return execute(registry, executor);
    }

    private static <R> R execute(ExDataLoaderRegistry registry, DataLoaderExecutor<R> executor) {
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
            return executor.execute(registry);
        } finally {
            // 如果是最外层设置registry的方法，则最后需要清除registry
            if (isOutermost) {
                RegistryHolder.clear();
            }
        }
    }

}