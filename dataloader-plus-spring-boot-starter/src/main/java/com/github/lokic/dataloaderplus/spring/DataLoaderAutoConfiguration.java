package com.github.lokic.dataloaderplus.spring;

import com.github.lokic.dataloaderplus.core.DataLoaderFactory;
import com.github.lokic.dataloaderplus.core.DataLoaderTemplate;
import com.github.lokic.dataloaderplus.core.TemplateConfig;
import org.dataloader.DataLoaderOptions;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Role;

import static org.springframework.core.Ordered.LOWEST_PRECEDENCE;

@AutoConfigureAfter({DataLoaderRegistrar.class})
@Import(DataLoaderRegistrar.class)
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
    public DataLoaderMappingBeanFactoryPointcutAdvisor dataLoaderMappingBeanFactoryPointcutAdvisor() {
        DataLoaderMappingBeanFactoryPointcutAdvisor advisor = new DataLoaderMappingBeanFactoryPointcutAdvisor();
        advisor.setAdvice(dataLoadableInterceptor());
        advisor.setOrder(LOWEST_PRECEDENCE);
        return advisor;
    }

    @Bean
    public DataLoaderInterceptor dataLoadableInterceptor() {
        return new DataLoaderInterceptor(dataLoaderTemplate());
    }


    @Bean
    public DataLoaderTemplate dataLoaderTemplate() {
        TemplateConfig templateConfig = TemplateConfig.builder()
                .options(DataLoaderOptions.newOptions())
                .factory(new DataLoaderFactory())
                .build();
        return new DataLoaderTemplate(templateConfig);
    }


    @Bean
    public BatchLoaderBeanPostProcessor dataLoaderTemplateBeanPostProcessor() {
        return new BatchLoaderBeanPostProcessor();
    }


}
