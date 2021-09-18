package com.github.lokic.dataloaderplus.spring.service;

import com.github.lokic.dataloaderplus.core.MultiKeyMappedBatchLoader;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.dataloader.BatchLoaderEnvironment;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Slf4j
public class UserAddressBatchLoader implements MultiKeyMappedBatchLoader<String, String> {

    @SneakyThrows
    @Override
    public Map<String, String> doLoad(Set<String> set, BatchLoaderEnvironment batchLoaderEnvironment) {
        log.debug("UserAddressBatchLoader start");
        return set.stream().collect(Collectors.toMap(Function.identity(), x -> "address:" + x));
    }
}