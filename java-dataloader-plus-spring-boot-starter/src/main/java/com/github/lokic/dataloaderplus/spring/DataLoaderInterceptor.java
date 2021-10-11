package com.github.lokic.dataloaderplus.spring;

import com.github.lokic.dataloaderplus.core.DataLoaderCallback;
import com.github.lokic.dataloaderplus.core.DataLoaderTemplate;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.util.concurrent.CompletableFuture;

public class DataLoaderInterceptor implements MethodInterceptor {

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

        DataLoaderTemplate template = manager.newTemplate();
        return template.using(callback);
    }

}