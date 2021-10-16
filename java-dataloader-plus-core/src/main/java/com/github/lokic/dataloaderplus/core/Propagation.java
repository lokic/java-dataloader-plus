package com.github.lokic.dataloaderplus.core;

public enum Propagation {

    /**
     * 如果当前存在Registry，则复用Registry的上下文，如果没有则新建一个Registry执行
     */
    REQUIRED,

    /**
     * 新建一个Registry执行
     */
    REQUIRES_NEW
}
