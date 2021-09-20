package com.github.lokic.dataloaderplus.spring;

import com.github.lokic.dataloaderplus.spring.annotation.DataLoadable;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;
import java.util.Objects;

public class DataLoadableBeanFactoryPointcutAdvisor extends AbstractBeanFactoryPointcutAdvisor {

    private final DataLoadablePointcut pointcut;

    public DataLoadableBeanFactoryPointcutAdvisor() {
        this.pointcut = new DataLoadablePointcut();
    }

    @Override
    public Pointcut getPointcut() {
        return pointcut;
    }

    public static class DataLoadablePointcut extends StaticMethodMatcherPointcut {

        @Override
        public boolean matches(Method method, Class<?> targetClass) {
            return Objects.nonNull(AnnotationUtils.findAnnotation(method, DataLoadable.class));
        }
    }
}
