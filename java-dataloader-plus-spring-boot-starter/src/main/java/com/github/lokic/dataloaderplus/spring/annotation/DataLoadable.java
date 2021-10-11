package com.github.lokic.dataloaderplus.spring.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface DataLoadable {

    /**
     * @see org.dataloader.DataLoaderOptions#batchingEnabled()
     */
    boolean batchingEnabled() default true;

    /**
     * @see org.dataloader.DataLoaderOptions#cachingEnabled()
     */
    boolean cachingEnabled() default true;

    /**
     * @see org.dataloader.DataLoaderOptions#cachingExceptionsEnabled()
     */
    boolean cachingExceptionsEnabled() default true;

    /**
     * @see org.dataloader.DataLoaderOptions#maxBatchSize()
     */
    int maxBatchSize() default -1;

}
