package com.github.lokic.dataloaderplus.spring.service;

import com.github.lokic.dataloaderplus.core.Propagation;
import com.github.lokic.dataloaderplus.spring.annotation.DataLoadable;
import com.github.lokic.dataloaderplus.spring.client.UserClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

import static com.github.lokic.javaplus.CompletableFutures.Fors.For;
import static com.github.lokic.javaplus.CompletableFutures.Fors.Yield;

@Service
public class UserService {

    @Autowired
    private UserClient userService;

    @DataLoadable(propagation = Propagation.REQUIRES_NEW)
    public CompletableFuture<String> getNameNew(String uid) {
        return CompletableFuture.completedFuture(uid)
                .thenCompose(For(userService::getNameById))
                .thenApply(Yield((id, name) -> name));
    }

    @DataLoadable
    public CompletableFuture<String> getName(String uid) {
        return CompletableFuture.completedFuture(uid)
                .thenCompose(For(userService::getNameById))
                .thenApply(Yield((id, name) -> name));
    }
}
