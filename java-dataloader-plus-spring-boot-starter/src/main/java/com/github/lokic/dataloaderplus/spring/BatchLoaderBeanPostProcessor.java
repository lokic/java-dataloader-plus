package com.github.lokic.dataloaderplus.spring;

import com.github.lokic.dataloaderplus.core.DataLoaderFactory;
import com.github.lokic.dataloaderplus.core.MultiKeyMappedBatchLoader;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.core.Ordered;

public class BatchLoaderBeanPostProcessor extends InstantiationAwareBeanPostProcessorAdapter implements BeanFactoryAware, Ordered {

    private DefaultListableBeanFactory beanFactory;


    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        if (beanFactory instanceof DefaultListableBeanFactory) {
            this.beanFactory = (DefaultListableBeanFactory) beanFactory;
        }
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof MultiKeyMappedBatchLoader) {
            DataLoaderFactory dataLoaderFactory = beanFactory.getBean(DataLoaderFactory.class);
            // 此处必须是beanName，否则后面基于class name会找不到对应的MultiKeyMappedBatchLoader，
            // 因为字节码增强之后，bean具体实现类名已经改变
            dataLoaderFactory.addMultiKeyMappedBatchLoader(beanName, (MultiKeyMappedBatchLoader<?, ?>) bean);
        }
        return bean;
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
