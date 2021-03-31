package me.lokic.dataloaderplus.core.annotation;

import me.lokic.dataloaderplus.core.MultiKeyMappedBatchLoader;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;

/**
 * dataloader的方法
 */
@Target({METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataLoaderMethod {

    /**
     * 对应的 MultiKeyMappedBatchLoader 的实现类，实际会通过 {@link me.lokic.dataloaderplus.core.DataLoaderFactory} 进行注册，
     * 然后创建对应的 {@link org.dataloader.DataLoader}
     *
     * @return
     */
    Class<? extends MultiKeyMappedBatchLoader<?, ?>> batchLoader();
}