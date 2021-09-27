package com.github.lokic.dataloaderplus.core.service;

import com.github.lokic.dataloaderplus.core.annotation.DataLoaderMapping;
import com.github.lokic.dataloaderplus.core.annotation.DataLoaderService;

import java.util.concurrent.CompletableFuture;

@DataLoaderService
public interface UserService {

    @DataLoaderMapping(batchLoader = UserNameBatchLoader.class)
    CompletableFuture<String> getNameById(String id);
}
