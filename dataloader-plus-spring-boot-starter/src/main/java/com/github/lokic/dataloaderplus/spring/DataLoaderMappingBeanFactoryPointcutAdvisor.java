package com.github.lokic.dataloaderplus.spring;

import com.github.lokic.dataloaderplus.core.annotation.DataLoaderMapping;
import com.github.lokic.dataloaderplus.core.annotation.DataLoaderService;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;
import java.util.Objects;

public class DataLoaderMappingBeanFactoryPointcutAdvisor extends AbstractBeanFactoryPointcutAdvisor {

    private final DataLoaderMappingPointcut pointcut;

    public DataLoaderMappingBeanFactoryPointcutAdvisor() {
        this.pointcut = new DataLoaderMappingPointcut();
    }

    @Override
    public Pointcut getPointcut() {
        return pointcut;
    }

    public static class DataLoaderMappingPointcut extends StaticMethodMatcherPointcut {

        @Override
        public boolean matches(Method method, Class<?> targetClass) {
            return Objects.nonNull(AnnotationUtils.findAnnotation(targetClass, DataLoaderService.class))
                    && Objects.nonNull(AnnotationUtils.findAnnotation(method, DataLoaderMapping.class));
        }
    }
}
