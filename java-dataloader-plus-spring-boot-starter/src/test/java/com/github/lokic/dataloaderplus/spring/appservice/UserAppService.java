package com.github.lokic.dataloaderplus.spring.appservice;

import com.github.lokic.dataloaderplus.spring.annotation.DataLoadable;
import com.github.lokic.dataloaderplus.spring.client.UserClient;
import com.github.lokic.dataloaderplus.spring.entity.UserInfo;
import com.github.lokic.dataloaderplus.spring.service.UserService;
import com.github.lokic.javaplus.CompletableFutures;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.github.lokic.javaplus.CompletableFutures.Fors.For;
import static com.github.lokic.javaplus.CompletableFutures.Fors.Yield;

@Service
public class UserAppService {

    @Autowired
    private UserClient userClient;

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
                .thenCompose(For(userClient::getNameById))
                .thenCompose(For((id, name) -> userClient.getAddressById(id)))
                .thenApply(Yield((id, name, address) ->
                        new StringJoiner(",").add(id).add(name).add(address).toString()));
    }

    @DataLoadable
    public CompletableFuture<UserInfo> getUserInfo(String uid) {
        return userClient.getNameById(uid)
                .thenCompose(For((name) -> userClient.getAddressById(uid)))
                .thenApply(Yield((name, address) -> new UserInfo(uid, name, address)));
    }

    @DataLoadable
    public CompletableFuture<List<UserInfo>> getUserInfoList(List<String> uidList) {
        List<CompletableFuture<UserInfo>> li = uidList.stream()
                .map(this::getUserInfo)
                .collect(Collectors.toList());
        return CompletableFutures.sequence(li);
    }

    @DataLoadable
    public CompletableFuture<String> getNew(String uid) {
        return userService.getNameNew(uid)
                .thenCompose(For((name) -> userClient.getAddressById(uid)))
                .thenApply(Yield((name, address) ->
                        new StringJoiner(",").add(uid).add(name).add(address).toString()));
    }

    @DataLoadable
    public CompletableFuture<String> getRepeatNew(String uid) {
        return userService.getNameNew(uid)
                .thenCompose(For((name1) -> userClient.getNameById(uid)))
                .thenApply(Yield((name1, name2) ->
                        new StringJoiner(",").add(uid).add(name1).add(name2).toString()));
    }

    @DataLoadable
    public CompletableFuture<String> getNest(String uid) {
        return userService.getName(uid)
                .thenCompose(For((name1) -> userClient.getNameById(uid)))
                .thenApply(Yield((name1, name2) ->
                        new StringJoiner(",").add(uid).add(name1).add(name2).toString()));
    }

}
