package com.github.lokic.dataloaderplus.core;


import com.github.lokic.dataloaderplus.core.tuples.Tuple3;

public interface MultiKey3MappedBatchLoader<K1, K2, K3, V> extends MultiKeyMappedBatchLoader<Tuple3<K1, K2, K3>, V> {

}
