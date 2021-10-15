package com.github.lokic.dataloaderplus.spring;

import com.github.lokic.dataloaderplus.core.DataLoaderCallback;
import com.github.lokic.dataloaderplus.core.DataLoaderTemplate;
import com.github.lokic.dataloaderplus.core.RegistryManager;
import com.github.lokic.dataloaderplus.spring.annotation.DataLoadable;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class DataLoaderInterceptor implements MethodInterceptor {

    private final Map<Method, DataLoaderAttribute> attributeMapping = new ConcurrentHashMap<>();

    private final DataLoaderTemplateManager manager;

    public DataLoaderInterceptor(DataLoaderTemplateManager manager) {
        this.manager = manager;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        @SuppressWarnings("unchecked")
        DataLoaderCallback<CompletableFuture<Object>> callback = reg -> {
            Object result = invocation.proceed();
            if (result instanceof CompletableFuture) {
                return (CompletableFuture<Object>) result;
            }
            throw new IllegalArgumentException("return type need CompletableFuture");
        };
        DataLoaderAttribute attribute = attributeMapping.computeIfAbsent(invocation.getMethod(), this::parseAttribute);
        DataLoaderTemplate template = manager.newTemplate(attribute);
        return template.using(RegistryManager.getRegistry(), callback);
    }

    private DataLoaderAttribute parseAttribute(Method method) {
        DataLoadable dataLoadable = AnnotationUtils.findAnnotation(method, DataLoadable.class);
        if (dataLoadable == null) {
            throw new IllegalStateException("not found @DataLoadable at method " + method.getName());
        }
        return DataLoaderAttribute.parseAttribute(dataLoadable);
    }

}