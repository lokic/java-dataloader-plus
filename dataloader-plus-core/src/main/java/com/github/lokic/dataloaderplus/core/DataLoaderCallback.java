package com.github.lokic.dataloaderplus.core;


@FunctionalInterface
public interface DataLoaderCallback<R> {

    R doInDataLoader(ExDataLoaderRegistry registry);
}
