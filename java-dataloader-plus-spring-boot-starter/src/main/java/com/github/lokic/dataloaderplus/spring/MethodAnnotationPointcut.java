package com.github.lokic.dataloaderplus.spring;

import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Objects;

public class MethodAnnotationPointcut extends StaticMethodMatcherPointcut {

    private final Class<? extends Annotation> methodAnnotationType;

    public MethodAnnotationPointcut(Class<? extends Annotation> methodAnnotationType) {
        this.methodAnnotationType = methodAnnotationType;
    }

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        return Objects.nonNull(AnnotationUtils.findAnnotation(method, methodAnnotationType));
    }
}
