package com.github.lokic.dataloaderplus.spring;

import com.github.lokic.dataloaderplus.core.DataLoaderCallback;
import com.github.lokic.dataloaderplus.core.DataLoaderTemplate;
import com.github.lokic.dataloaderplus.core.ExDataLoaderRegistry;
import com.github.lokic.dataloaderplus.core.RegistryHolder;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.util.concurrent.CompletableFuture;

public class DataLoaderInterceptor implements MethodInterceptor {

    private final DataLoaderTemplate template;

    public DataLoaderInterceptor(DataLoaderTemplate template) {
        this.template = template;
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

        ExDataLoaderRegistry registry = RegistryHolder.getRegistry();
        if (registry == null) {
            return template.using(callback);
        } else {
            return template.using(registry, callback);
        }
    }

}