package com.github.lokic.dataloaderplus.spring.appservice;

import com.github.lokic.dataloaderplus.spring.annotation.DataLoadable;
import com.github.lokic.dataloaderplus.spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.StringJoiner;
import java.util.concurrent.CompletableFuture;

import static com.github.lokic.javaplus.CompletableFutures.Fors.For;
import static com.github.lokic.javaplus.CompletableFutures.Fors.Yield;

@Service
public class UserAppService {

    @Autowired
    private UserService userService;


    @DataLoadable
    public CompletableFuture<String> getThrow(String uid) {
        CompletableFuture<String> f = new CompletableFuture<>();
        f.completeExceptionally(new IllegalArgumentException("illegal uid"));
        return f;
    }


    @DataLoadable
    public CompletableFuture<String> get(String uid) {
        return CompletableFuture.completedFuture(uid)
                .thenCompose(For(userService::getNameById))
                .thenCompose(For((id, name) -> userService.getAddressById(id)))
                .thenApply(Yield((id, name, address) ->
                        new StringJoiner(",").add(id).add(name).add(address).toString()));
    }

}
