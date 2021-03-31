package me.lokic.dataloaderplus.core;


import me.lokic.dataloaderplus.core.tuples.Tuple2;

public interface MultiKey2MappedBatchLoader<K1, K2, V> extends MultiKeyMappedBatchLoader<Tuple2<K1, K2>, V> {


}
