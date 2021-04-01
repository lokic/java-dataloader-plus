package me.lokic.dataloaderplus.core.service;

import me.lokic.dataloaderplus.core.annotation.DataLoaderMethod;
import me.lokic.dataloaderplus.core.annotation.DataLoaderService;

import java.util.concurrent.CompletableFuture;

@DataLoaderService
public interface UserService {

    @DataLoaderMethod(batchLoader = UserNameBatchLoader.class)
    CompletableFuture<String> getNameById(String id);
}
