package com.github.lokic.dataloaderplus.spring;

import com.github.lokic.dataloaderplus.core.DataLoaderTemplate;
import com.github.lokic.dataloaderplus.spring.annotation.EnableDataLoader;
import com.github.lokic.dataloaderplus.spring.service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@EnableDataLoader
public class EnableDataLoaderTest {

    @Autowired
    private ApplicationContext context;


    @Test
    public void test_inject() {
        Assertions.assertThat(context.getBeansOfType(UserService.class)).hasSize(1);
        Assertions.assertThat(context.getBeansOfType(DataLoaderTemplate.class)).hasSize(1);
    }


}
