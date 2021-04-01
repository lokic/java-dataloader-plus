package me.lokic.dataloaderplus.core.service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.lokic.dataloaderplus.core.MultiKeyMappedBatchLoader;
import org.dataloader.BatchLoaderEnvironment;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class UserNameBatchLoader implements MultiKeyMappedBatchLoader<String, String> {

    @SneakyThrows
    @Override
    public CompletionStage<Map<String, String>> load(Set<String> set, BatchLoaderEnvironment batchLoaderEnvironment) {
        return CompletableFuture.supplyAsync(() -> set.stream().collect(Collectors.toMap(Function.identity(), x -> "name:" + x)));
    }
}
