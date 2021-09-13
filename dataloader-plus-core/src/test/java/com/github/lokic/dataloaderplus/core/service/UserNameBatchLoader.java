package com.github.lokic.dataloaderplus.core.service;

import com.github.lokic.dataloaderplus.core.MultiKeyMappedBatchLoader;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
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
    public CompletionStage<Map<String, String>> doLoad(Set<String> set, BatchLoaderEnvironment batchLoaderEnvironment) {
        log.info("UserNameBatchLoader1 start");
        return CompletableFuture.supplyAsync(() -> {
            log.info("UserNameBatchLoader1 supplyAsync start");
            return set.stream()
                    .collect(Collectors.toMap(Function.identity(), x -> "name:" + x));
        });
//        return CompletableFuture.completedFuture(set.stream()
//                    .collect(Collectors.toMap(Function.identity(), x -> "name:" + x)));


//        Thread.sleep(1000L);
//      return CompletableFutures.sequence(set.stream()
//                .collect(Collectors.toMap(Function.identity(), x ->  {
//                    log.info("UserNameBatchLoader1 supplyAsync start " + x);
//                    if(x.endsWith("xx")){
//                        return CompletableFuture.completedFuture("end end " + x);
//                    }
//                    return userService2.getNameById(x + "x");
//                })));
    }
}
