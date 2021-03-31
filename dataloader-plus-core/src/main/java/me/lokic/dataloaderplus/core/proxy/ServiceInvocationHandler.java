package me.lokic.dataloaderplus.core.proxy;

import lombok.extern.slf4j.Slf4j;
import me.lokic.dataloaderplus.core.ExDataLoaderRegistry;
import me.lokic.dataloaderplus.core.RegistryHolder;
import me.lokic.dataloaderplus.core.annotation.DataLoaderMethod;
import me.lokic.dataloaderplus.core.annotation.KeyContext;
import me.lokic.dataloaderplus.core.kits.Arrays;
import me.lokic.dataloaderplus.core.tuples.Tuple;
import org.dataloader.DataLoader;
import org.dataloader.MappedBatchLoaderWithContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class ServiceInvocationHandler implements InvocationHandler {

    public static final ServiceInvocationHandler INSTANCE = new ServiceInvocationHandler();


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        DataLoaderMethod dataLoaderMethod = method.getAnnotation(DataLoaderMethod.class);
        if (dataLoaderMethod == null) {
            throw new IllegalArgumentException("without @DataLoaderMethod");
        }

        if (!method.getReturnType().isAssignableFrom(CompletableFuture.class)) {
            throw new IllegalArgumentException("return type need CompletableFuture");
        }

        ExDataLoaderRegistry registry = RegistryHolder.getRegistry();
        if (registry == null) {
            throw new IllegalArgumentException("@DataLoaderService need in DataLoaderTemplate block");
        }

        int contextIndex = findContextIndex(method);
        Object[] noKeyContextArgs = Arrays.remove(args, contextIndex);

        if (noKeyContextArgs.length == 0) {
            throw new IllegalArgumentException("no args method not support @DataLoaderMethod");
        }

        Class<? extends MappedBatchLoaderWithContext<?, ?>> provider = dataLoaderMethod.batchLoader();
        DataLoader<Object, Object> dataLoader = registry.getDataLoader(provider);

        Object loadKey = buildLoadKey(noKeyContextArgs);

        if (contextIndex != -1) {
            return dataLoader.load(loadKey, args[contextIndex]);
        }
        return dataLoader.load(loadKey);
    }


    /**
     * 由于一个参数的时候，没有元组，所以就直接使用入参，不转换元组
     *
     * @param noKeyContextArgs
     * @return
     */
    private Object buildLoadKey(Object[] noKeyContextArgs) {
        return noKeyContextArgs.length == 1 ? noKeyContextArgs[0] : Tuple.fromArray(noKeyContextArgs);
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
