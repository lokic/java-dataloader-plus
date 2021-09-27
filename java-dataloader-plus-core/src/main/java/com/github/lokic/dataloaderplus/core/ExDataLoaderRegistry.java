package com.github.lokic.dataloaderplus.core;

import com.github.lokic.dataloaderplus.core.annotation.DataLoaderService;
import com.github.lokic.dataloaderplus.core.proxy.InterfaceProxy;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderOptions;
import org.dataloader.DataLoaderRegistry;
import org.dataloader.MappedBatchLoaderWithContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * DataLoaderRegistry的扩展
 */
@Slf4j
public class ExDataLoaderRegistry implements AutoCloseable {

    private final DataLoaderOptions options;

    private final DataLoaderFactory factory;

    private final DataLoaderRegistry registry;

    /**
     * 使用 @{@link DataLoaderService} 注释接口的实现类的缓存，避免重复创建
     */
    private static final Map<Class<?>, Object> SERVICE_CACHE = new ConcurrentHashMap<>();

    protected ExDataLoaderRegistry(
            @NonNull DataLoaderOptions options,
            @NonNull DataLoaderFactory factory,
            @NonNull DataLoaderRegistry registry) {
        this.registry = registry;
        this.factory = factory;
        this.options = options;
    }

    /**
     * 获取对应的 {@link DataLoader}，如果没有获取到则创建一个并返回
     *
     * @param clazz
     * @param <K>
     * @param <V>
     * @return
     */
    public <K, V> DataLoader<K, V> getOrRegisterDataLoader(Class<? extends MappedBatchLoaderWithContext<?, ?>> clazz) {
        return registry.computeIfAbsent(clazz.getName(), key -> factory.create(key, options));
    }

    public void dispatchAll() {
        registry.getDataLoaders().forEach(this::dispatch);
    }

    private void dispatch(DataLoader<?, ?> dataLoader) {
        dataLoader.dispatch()
                .thenAccept(li -> {
                    if (!li.isEmpty()) {
                        RegistryHolder.tryDispatchAll();
                    }
                });
    }

    /**
     * 获取 {@link DataLoaderService} 对应的 {@code serviceClazz} 的实现
     *
     * @param serviceClazz
     * @param <S>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <S> S getService(Class<S> serviceClazz) {
        return (S) SERVICE_CACHE.computeIfAbsent(
                serviceClazz,
                InterfaceProxy::newInstance
        );
    }

    @SneakyThrows
    @Override
    public void close() {
        if (registry instanceof AutoCloseable) {
            ((AutoCloseable) registry).close();
        }
    }
}
