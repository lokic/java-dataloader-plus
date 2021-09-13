package com.github.lokic.dataloaderplus.core;

@FunctionalInterface
public interface RegisterCallback {
    void doInRegister(ExDataLoaderRegistry registry);
}
