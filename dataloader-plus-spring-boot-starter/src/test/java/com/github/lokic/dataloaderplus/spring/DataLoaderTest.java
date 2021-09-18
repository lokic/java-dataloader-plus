package com.github.lokic.dataloaderplus.spring;

import com.github.lokic.dataloaderplus.spring.annotation.EnableDataLoader;
import com.github.lokic.dataloaderplus.spring.appservice.UserAppService;
import com.github.lokic.dataloaderplus.spring.service.UserAddressBatchLoader;
import com.github.lokic.dataloaderplus.spring.service.UserService;
import com.github.lokic.javaplus.CompletableFutures;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@EnableDataLoader
@Import({UserAppService.class, UserAddressBatchLoader.class})
public class DataLoaderTest {

    @Autowired
    private UserAppService userAppService;

    @Autowired
    private UserService userService;

    @SpyBean
    private DataLoaderInterceptor dataLoaderInterceptor;

    @Test(expected = IllegalArgumentException.class)
    public void test_DataLoadable_throw_in_template() {
        CompletableFutures.getOrElseSneakyThrow(userAppService.getThrow("123"));
    }

    @Test
    public void test_DataLoadable_get_in_template() {
        Assert.assertEquals("123,name:123,address:123", CompletableFutures.getOrElseSneakyThrow(userAppService.get("123")));
    }

    @Test
    public void test_DataLoaderMapping_in_template() throws Throwable {
        Assert.assertEquals("name:123", userService.getNameById("123").join());
        Mockito.verify(dataLoaderInterceptor, Mockito.times(1))
                .invoke(Mockito.any(MethodInvocation.class));
    }

}
