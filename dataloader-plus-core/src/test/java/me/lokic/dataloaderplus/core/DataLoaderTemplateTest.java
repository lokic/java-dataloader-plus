package me.lokic.dataloaderplus.core;

import me.lokic.dataloaderplus.core.kits.CompletableFutures;
import me.lokic.dataloaderplus.core.service.UserNameBatchLoader;
import me.lokic.dataloaderplus.core.service.UserService;
import org.dataloader.BatchLoaderEnvironment;
import org.dataloader.DataLoader;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class DataLoaderTemplateTest {


    /**
     * 多次执行dataloader，方法{@link UserNameBatchLoader#load(Set, BatchLoaderEnvironment)} 调用一次
     */
    @Test
    public void testBatchLoaderCallOneTimes() {

        UserNameBatchLoader userNameBatchLoader = Mockito.spy(new UserNameBatchLoader());

        DataLoader<?, ?> dataLoader = DataLoader.newMappedDataLoader(userNameBatchLoader, TemplateConfig.DEFAULT.getOptions());
        DataLoaderFactory factory = Mockito.mock(DataLoaderFactory.class);
        Mockito.doReturn(dataLoader).when(factory).create(Mockito.any(), Mockito.any());

        DataLoaderTemplate template = new DataLoaderTemplate(TemplateConfig.builder().factory(factory).build());

        UserService userService = ExDataLoaderRegistry.getService(UserService.class);
        List<String> res = template.using(registry -> {
            List<CompletableFuture<String>> li = new ArrayList<>();
            li.add(userService.getNameById("1"));
            li.add(userService.getNameById("2"));
            return CompletableFutures.sequence(li);
        });


        List<String> expectedRes = new ArrayList<>();
        expectedRes.add("name:1");
        expectedRes.add("name:2");
        Assert.assertEquals(expectedRes, res);


        Set<String> set = new HashSet<>();
        set.add("1");
        set.add("2");
        Mockito.verify(userNameBatchLoader, Mockito.times(1))
                .load(Mockito.eq(set), Mockito.any());

    }

}