package com.github.lokic.dataloaderplus.core;

import com.github.lokic.dataloaderplus.core.kits.CompletableFutures;
import com.github.lokic.dataloaderplus.core.service.UserAddressBatchLoader;
import com.github.lokic.dataloaderplus.core.service.UserNameBatchLoader;
import com.github.lokic.dataloaderplus.core.service.UserService;
import com.github.lokic.dataloaderplus.core.service.UserService2;
import lombok.extern.slf4j.Slf4j;
import org.dataloader.BatchLoaderEnvironment;
import org.dataloader.DataLoaderOptions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.github.lokic.javaplus.CompletableFutures.Fors.For;
import static com.github.lokic.javaplus.CompletableFutures.Fors.Yield;

@Slf4j
public class DataLoaderTemplateTest {

    private DataLoaderTemplate template;

    private TemplateConfig templateConfig;

    private UserService userService;

    private UserService2 userService2;

    @Before
    public void init() {

        UserAddressBatchLoader userAddressBatchLoader = Mockito.spy(new UserAddressBatchLoader());
        userService2 = ExDataLoaderRegistry.getService(UserService2.class);
        userService = ExDataLoaderRegistry.getService(UserService.class);

        UserNameBatchLoader userNameBatchLoader = Mockito.spy(new UserNameBatchLoader());
        DataLoaderFactory factory = new DataLoaderFactory();
        factory.addMultiKeyMappedBatchLoader(UserNameBatchLoader.class.getName(), userNameBatchLoader);
        factory.addMultiKeyMappedBatchLoader(UserAddressBatchLoader.class.getName(), userAddressBatchLoader);

        templateConfig = TemplateConfig.builder().options(DataLoaderOptions.newOptions()).factory(factory).build();
        template = new DataLoaderTemplate(templateConfig);

    }

    /**
     * 多次执行dataloader，方法{@link UserNameBatchLoader#load(Set, BatchLoaderEnvironment)} 调用一次
     */
    @Test
    public void testBatchLoaderCallOneTimes() {

        Set<String> set = new HashSet<>();

        for (int i = 1; i < 3; i++) {
            set.add("" + i);
        }

        List<UserInfo> res =
                template.using(
                        RegistryFactoryType.DEFAULT,
                        reg -> reg.register(UserNameBatchLoader.class)
                                .register(UserAddressBatchLoader.class),
                        reg -> {
                            List<CompletableFuture<UserInfo>> li = set.stream()
                                    .map(this::process)
                                    .collect(Collectors.toList());
                            return CompletableFutures.sequence(li);
                        });
        List<String> strings = res.stream()
                .map(u -> new StringJoiner(",")
                        .add(u.getId()).add(u.getName()).add(u.getAddress())
                        .toString())
                .collect(Collectors.toList());

        List<String> expectedRes = new ArrayList<>();
        expectedRes.add("1" + "," + "name:1" + "," + "address:1");
        expectedRes.add("2" + "," + "name:2" + "," + "address:2");
        Assert.assertEquals(expectedRes, strings);

        UserNameBatchLoader userNameBatchLoader = (UserNameBatchLoader) templateConfig.getFactory().getMultiKeyMappedBatchLoader(UserNameBatchLoader.class.getName());
        UserAddressBatchLoader userAddressBatchLoader = (UserAddressBatchLoader) templateConfig.getFactory().getMultiKeyMappedBatchLoader(UserAddressBatchLoader.class.getName());

        Mockito.verify(userNameBatchLoader, Mockito.times(1))
                .load(Mockito.eq(set), Mockito.any());

        Mockito.verify(userAddressBatchLoader, Mockito.times(1))
                .load(Mockito.eq(set), Mockito.any());

    }

    private CompletableFuture<UserInfo> process(String userId) {
        return CompletableFuture.completedFuture(userId)
                .thenCompose(For(userService::getAddressById))
                .thenCompose(For((uid, name) -> userService2.getNameById(uid)))
                .thenApply(Yield(UserInfo::new));
    }

    public static class UserInfo {
        private final String id;
        private final String name;
        private final String address;

        public UserInfo(String id, String name, String address) {
            this.id = id;
            this.name = name;
            this.address = address;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getAddress() {
            return address;
        }
    }

}