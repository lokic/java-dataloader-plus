package com.github.lokic.dataloaderplus.core.proxy;

import com.github.lokic.dataloaderplus.core.ExDataLoaderRegistry;
import com.github.lokic.dataloaderplus.core.MultiKeyMappedBatchLoader;
import com.github.lokic.dataloaderplus.core.RegistryHolder;
import com.github.lokic.dataloaderplus.core.annotation.DataLoaderMethod;
import com.github.lokic.dataloaderplus.core.annotation.KeyContext;
import com.github.lokic.dataloaderplus.core.kits.Arrays;
import com.github.lokic.dataloaderplus.core.tuples.Tuple;
import lombok.extern.slf4j.Slf4j;
import org.dataloader.DataLoader;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ServiceInvocationHandler implements InvocationHandler {

    public static final ServiceInvocationHandler INSTANCE = new ServiceInvocationHandler();

    private final Map<Method, Class<? extends MultiKeyMappedBatchLoader<?, ?>>> batchLoaderMapping = new ConcurrentHashMap<>();
    private final Map<Method, Integer> keyContextIndexMapping = new ConcurrentHashMap<>();

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        ExDataLoaderRegistry registry = RegistryHolder.getRegistry();
        if (registry == null) {
            //see https://www.graphql-java.com/documentation/v16/batching/
            throw new IllegalStateException("@DataLoaderService need in DataLoaderTemplate block " +
                    "or call to a DataLoader in an asynchronous off thread");
        }
        Class<? extends MultiKeyMappedBatchLoader<?, ?>> provider = batchLoaderMapping.computeIfAbsent(method, this::findBatchLoaderClass);
        int keyContextIndex = keyContextIndexMapping.computeIfAbsent(method, this::findContextIndex);
        DataLoader<Object, Object> dataLoader = registry.getOrRegisterDataLoader(provider);
        if (dataLoader == null) {
            throw new IllegalStateException("not register MappedBatchLoaderWithContext = " + provider.getName() + " in registry");
        }
        Object key = buildKey(args, keyContextIndex);
        Object keyContext = buildKeyContext(args, keyContextIndex);
        return dataLoader.load(key, keyContext);
    }

    /**
     * 查找方法对应的MultiKeyMappedBatchLoader
     *
     * @param method
     * @return
     */
    private Class<? extends MultiKeyMappedBatchLoader<?, ?>> findBatchLoaderClass(Method method) {
        DataLoaderMethod dataLoaderMethod = method.getAnnotation(DataLoaderMethod.class);
        if (dataLoaderMethod == null) {
            throw new IllegalArgumentException("without @DataLoaderMethod");
        }
        if (!method.getReturnType().isAssignableFrom(CompletableFuture.class)) {
            throw new IllegalArgumentException("return type need CompletableFuture");
        }
        return dataLoaderMethod.batchLoader();
    }

    /**
     * 由于一个参数的时候，没有元组，所以就直接使用入参，不转换元组
     *
     * @return
     */
    private Object buildKey(Object[] args, int contextIndex) {
        Object[] noKeyContextArgs = Arrays.remove(args, contextIndex);
        if (noKeyContextArgs.length == 0) {
            throw new IllegalArgumentException("no args method not support @DataLoaderMethod");
        }
        return noKeyContextArgs.length == 1 ? noKeyContextArgs[0] : Tuple.fromArray(noKeyContextArgs);
    }

    /**
     * 构建keyContext， 没有找到返回null
     *
     * @param args
     * @param contextIndex
     * @return
     */
    private Object buildKeyContext(Object[] args, int contextIndex) {
        if (contextIndex < 0 || contextIndex > args.length - 1) {
            return null;
        }
        return args[contextIndex];
    }

    /**
     * 查找使用 @{@link KeyContext} 注解的参数的index
     *
     * @param method
     * @return
     */
    private int findContextIndex(Method method) {
        int contextIndex = -1;
        for (int i = 0; i < method.getParameterAnnotations().length; i++) {
            Annotation[] paramAnnotations = method.getParameterAnnotations()[i];
            for (Annotation annotation : paramAnnotations) {
                if (annotation.annotationType() == KeyContext.class) {
                    if (contextIndex != -1) {
                        throw new IllegalArgumentException("@Context args, need only one");
                    }
                    contextIndex = i;
                }
            }
        }
        return contextIndex;
    }
}
