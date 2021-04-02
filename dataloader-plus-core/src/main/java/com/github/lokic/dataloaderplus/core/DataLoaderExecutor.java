package com.github.lokic.dataloaderplus.core;


@FunctionalInterface
public interface DataLoaderExecutor<R> {

    R execute(ExDataLoaderRegistry registry);
}
