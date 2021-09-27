package com.github.lokic.dataloaderplus.core.service;

import com.github.lokic.dataloaderplus.core.MultiKeyMappedBatchLoader;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.dataloader.BatchLoaderEnvironment;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class UserAddressBatchLoader implements MultiKeyMappedBatchLoader<String, String> {

    @SneakyThrows
    @Override
    public Map<String, String> doLoad(Set<String> set, BatchLoaderEnvironment batchLoaderEnvironment) {
        log.info("UserAddressBatchLoader start");
        return set.stream().collect(Collectors.toMap(Function.identity(), x -> "address:" + x));
    }
}
