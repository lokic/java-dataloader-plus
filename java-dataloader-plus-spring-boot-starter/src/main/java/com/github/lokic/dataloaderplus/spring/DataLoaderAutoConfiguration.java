package com.github.lokic.dataloaderplus.spring;

import com.github.lokic.dataloaderplus.core.DataLoaderFactory;
import org.dataloader.DataLoaderOptions;
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


    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    @Bean
    public DataLoadableBeanFactoryPointcutAdvisor dataLoadableBeanFactoryPointcutAdvisor() {
        DataLoadableBeanFactoryPointcutAdvisor advisor = new DataLoadableBeanFactoryPointcutAdvisor();
        advisor.setAdvice(dataLoadableInterceptor());
        advisor.setOrder(LOWEST_PRECEDENCE);
        return advisor;
    }

    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    @Bean
    public DataLoaderInterceptor dataLoadableInterceptor() {
        return new DataLoaderInterceptor(dataLoaderTemplateManager());
    }

    @Bean
    public DataLoaderTemplateFactory dataLoaderTemplateManager() {
        return new DataLoaderTemplateFactory(DataLoaderOptions.newOptions(), dataLoaderFactory());
    }

    @Bean
    public DataLoaderFactory dataLoaderFactory() {
        return new DataLoaderFactory();
    }

    @Bean
    public BatchLoaderBeanPostProcessor dataLoaderTemplateBeanPostProcessor() {
        return new BatchLoaderBeanPostProcessor();
    }


}
