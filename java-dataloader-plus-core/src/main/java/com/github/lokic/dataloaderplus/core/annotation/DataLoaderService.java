package com.github.lokic.dataloaderplus.core.annotation;

import java.lang.annotation.*;

/**
 * 用于标识是一个dataloader的服务
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataLoaderService {
}
