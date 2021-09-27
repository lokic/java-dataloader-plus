package com.github.lokic.dataloaderplus.core;

import com.github.lokic.javaplus.CompletableFutures;
import org.dataloader.BatchLoaderEnvironment;
import org.dataloader.MappedBatchLoaderWithContext;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletionStage;

/**
 * 用于对应一些多参数的方法，不需要去重新定义一个包含所有参数的参数对象
 * <p>
 * 如, <pre>{@code CompletableFuture<String> method(String p1, String p2)}</pre>
 * <p>
 * <a href="https://www.graphql-java.com/documentation/v16/batching/">graphql-java batching</a>
 *
 * @param <K>
 * @param <V>
 */

public interface MultiKeyMappedBatchLoader<K, V> extends MappedBatchLoaderWithContext<K, V> {

    @Override
    default CompletionStage<Map<K, V>> load(Set<K> keys, BatchLoaderEnvironment environment) {
        // 为了控制future的异步线程数，在这里完成future的同步执行，保证之后的操作还是在业务线程中执行
        return CompletableFutures.supply(() -> doLoad(keys, environment));
    }

    Map<K, V> doLoad(Set<K> keys, BatchLoaderEnvironment environment);

}
