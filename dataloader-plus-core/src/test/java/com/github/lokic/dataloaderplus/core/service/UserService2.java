package com.github.lokic.dataloaderplus.core.service;

import com.github.lokic.dataloaderplus.core.annotation.DataLoaderMethod;
import com.github.lokic.dataloaderplus.core.annotation.DataLoaderService;

import java.util.concurrent.CompletableFuture;

@DataLoaderService
public interface UserService2 {

    @DataLoaderMethod(batchLoader = UserAddressBatchLoader.class)
    CompletableFuture<String> getNameById(String id);
}
