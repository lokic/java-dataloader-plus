package me.lokic.dataloaderplus.core.kits;

import lombok.extern.slf4j.Slf4j;
import me.lokic.dataloaderplus.core.RegistryHolder;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
public class CompletableFutures {

    public static <T> CompletableFuture<List<T>> sequence(List<CompletableFuture<T>> futures) {
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]))
                .thenApply(v -> futures.stream().map(CompletableFuture::join).collect(Collectors.<T>toList()));
    }

    public static <K, V> CompletableFuture<Map<K, V>> sequence(Map<K, CompletableFuture<V>> futures) {
        return CompletableFuture.allOf(futures.values().toArray(new CompletableFuture[futures.size()]))
                .thenApply(v -> futures.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().join())));
    }


    public static <T> T join(CompletableFuture<T> future) {
        RegistryHolder.tryDispatchAll();
        return future.join();
    }
}
