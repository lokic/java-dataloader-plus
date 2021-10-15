package com.github.lokic.dataloaderplus.core;


@FunctionalInterface
public interface DataLoaderCallback<R> {

    R invokeInDataLoader(ExDataLoaderRegistry registry) throws Throwable;
}
