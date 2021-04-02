package com.github.lokic.dataloaderplus.core.service;

import com.github.lokic.dataloaderplus.core.annotation.DataLoaderMethod;
import com.github.lokic.dataloaderplus.core.annotation.DataLoaderService;

import java.util.concurrent.CompletableFuture;

@DataLoaderService
public interface UserService {

    @DataLoaderMethod(batchLoader = UserNameBatchLoader.class)
    CompletableFuture<String> getNameById(String id);
}
