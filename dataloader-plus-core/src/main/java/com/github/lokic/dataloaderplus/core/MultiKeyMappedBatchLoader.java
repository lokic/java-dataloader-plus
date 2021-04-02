package com.github.lokic.dataloaderplus.core;

import org.dataloader.MappedBatchLoaderWithContext;

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

}
