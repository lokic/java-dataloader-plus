package com.github.lokic.dataloaderplus.spring;

import com.github.lokic.dataloaderplus.core.AbstractDataLoaderFactory;
import com.github.lokic.dataloaderplus.core.DataLoaderFactoryImpl;
import com.github.lokic.dataloaderplus.spring.annotation.EnableDataLoader;
import com.github.lokic.dataloaderplus.spring.appservice.UserAppService;
import com.github.lokic.dataloaderplus.spring.client.UserAddressBatchLoader;
import com.github.lokic.dataloaderplus.spring.client.UserNameBatchLoader;
import com.github.lokic.dataloaderplus.spring.service.UserService;
import com.github.lokic.javaplus.CompletableFutures;
import lombok.SneakyThrows;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Field;
import java.util.Map;

@RunWith(SpringRunner.class)
@EnableDataLoader
@Import({UserAppService.class, UserService.class})
public class DataLoadableTest {

    @Autowired
    private UserAppService userAppService;

    @Autowired
    private SpringDataLoaderFactory springDataLoaderFactory;

    @SpyBean
    private UserNameBatchLoader userNameBatchLoader;

    @SpyBean
    private UserAddressBatchLoader userAddressBatchLoader;

    @SuppressWarnings("unchecked")
    @SneakyThrows
    @Before
    public void  init() {
        Field field = AbstractDataLoaderFactory.class.getDeclaredField("dataLoaderCreators");
        field.setAccessible(true);
        Map<Object, Object> map = (Map<Object, Object>) field.get(springDataLoaderFactory);
        map.put(UserNameBatchLoader.class, userNameBatchLoader);
        map.put(UserAddressBatchLoader.class, userAddressBatchLoader);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_DataLoadable_throw_in_template() {
        CompletableFutures.join(userAppService.getThrow("123"));
    }

    @Test
    public void test_DataLoadable_get_in_template() {
        Assert.assertEquals("123,name:123,address:123", CompletableFutures.join(userAppService.get("123")));
        Mockito.verify(userNameBatchLoader, Mockito.times(1))
                .doLoad(Mockito.any(), Mockito.any());
        Mockito.verify(userAddressBatchLoader, Mockito.times(1))
                .doLoad(Mockito.any(), Mockito.any());
    }

    @Test
    public void test_DataLoadable_getNew_in_template() throws Throwable {
        String res = CompletableFutures.join(userAppService.getNew("123"));
        Mockito.verify(userNameBatchLoader, Mockito.times(1))
                .doLoad(Mockito.any(), Mockito.any());
        Mockito.verify(userAddressBatchLoader, Mockito.times(1))
                .doLoad(Mockito.any(), Mockito.any());
        Assert.assertEquals("123,name:123,address:123", res);
    }

    @Test
    public void test_DataLoadable_getRepeatNew_in_template() throws Throwable {
        CompletableFutures.join(userAppService.getRepeatNew("123"));
        Mockito.verify(userNameBatchLoader, Mockito.times(2))
                .doLoad(Mockito.any(), Mockito.any());
    }

    @Test
    public void test_DataLoadable_getNest_in_template() throws Throwable {
        CompletableFutures.join(userAppService.getNest("123"));
        Mockito.verify(userNameBatchLoader, Mockito.times(1))
                .doLoad(Mockito.any(), Mockito.any());
    }
}
