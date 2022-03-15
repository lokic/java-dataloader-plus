package com.github.lokic.dataloaderplus.spring;

import com.github.lokic.dataloaderplus.core.DataLoaderFactory;
import com.github.lokic.dataloaderplus.core.DataLoaderFactoryImpl;
import org.dataloader.DataLoaderOptions;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;

import static org.springframework.core.Ordered.LOWEST_PRECEDENCE;

@AutoConfigureAfter({DataLoaderRegistrar.class})
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Configuration
public class DataLoaderAutoConfiguration {

    @Bean
    public DataLoaderPostProcessor dataLoaderPostProcessor(DataLoaderFactory dataLoaderFactory) {
        return new DataLoaderPostProcessor(dataLoaderFactory);
    }

    @Bean
    public DataLoaderFactory dataLoaderFactory(AutowireCapableBeanFactory autowireCapableBeanFactory) {
        return new SpringDataLoaderFactory(autowireCapableBeanFactory);
    }

}
