package com.github.lokic.dataloaderplus.core.service;

import com.github.lokic.dataloaderplus.core.annotation.DataLoaderMapping;
import com.github.lokic.dataloaderplus.core.annotation.DataLoaderService;

import java.util.concurrent.CompletableFuture;

@DataLoaderService
public interface UserService2 {

    @DataLoaderMapping(batchLoader = UserAddressBatchLoader.class)
    CompletableFuture<String> getAddressById(String id);
}
