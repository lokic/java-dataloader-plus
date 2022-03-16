package com.github.lokic.dataloaderplus.spring;

import com.github.lokic.dataloaderplus.core.DataLoaderFactory;
import com.github.lokic.dataloaderplus.spring.annotation.DataLoadable;
import org.dataloader.DataLoaderOptions;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.autoproxy.AbstractBeanFactoryAwareAdvisingPostProcessor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.InitializingBean;

public class DataLoaderPostProcessor extends AbstractBeanFactoryAwareAdvisingPostProcessor implements InitializingBean {

    private final DataLoaderTemplateFactory dataLoaderTemplateFactory;

    public DataLoaderPostProcessor(DataLoaderFactory dataLoaderFactory) {
        this.dataLoaderTemplateFactory = new DataLoaderTemplateFactory(DataLoaderOptions.newOptions(), dataLoaderFactory);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Pointcut pointcut = new MethodAnnotationPointcut(DataLoadable.class);
        this.advisor = new DefaultPointcutAdvisor(pointcut, new DataLoaderInterceptor(dataLoaderTemplateFactory));
    }


}
