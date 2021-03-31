package me.lokic.dataloaderplus.core;

import lombok.extern.slf4j.Slf4j;
import me.lokic.dataloaderplus.core.proxy.InterfaceProxy;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderOptions;
import org.dataloader.DataLoaderRegistry;
import org.dataloader.MappedBatchLoaderWithContext;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * DataLoaderRegistry的扩展
 */
@Slf4j
public class ExDataLoaderRegistry extends DataLoaderRegistry {

    private final DataLoaderOptions options;
    private final DataLoaderFactory factory;

    /**
     * 使用 @{@link me.lokic.dataloaderplus.core.annotation.DataLoaderService} 注释接口的实现类的缓存，避免重复创建
     */
    private static final Map<Class<?>, Object> SERVICE_CACHE = new ConcurrentHashMap<>();

    protected ExDataLoaderRegistry(DataLoaderOptions options, DataLoaderFactory factory) {
        this.options = options;
        this.factory = factory;
    }

    /**
     * 获取或者创建对应的 {@link DataLoader}
     *
     * @param clazz
     * @param <K>
     * @param <V>
     * @return
     */
    public <K, V> DataLoader<K, V> getDataLoader(Class<? extends MappedBatchLoaderWithContext<?, ?>> clazz) {
        return computeIfAbsent(clazz.getName(), key -> factory.create(key, options));
    }

    /**
     * 获取 {@link me.lokic.dataloaderplus.core.annotation.DataLoaderService} 对应的 {@code serviceClazz} 的实现
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


    @Override
    public void dispatchAll() {
        List<DataLoader<?, ?>> dataLoaders = getDataLoaders();
        dataLoaders.forEach(DataLoader::dispatch);
    }

}
