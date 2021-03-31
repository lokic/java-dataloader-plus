package me.lokic.dataloaderplus.core.tuples;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@ToString
@EqualsAndHashCode
public class Tuple2<T1, T2> implements Tuple, Serializable {

    private static final long serialVersionUID = -1655443004987467988L;

    private final T1 t1;
    private final T2 t2;

    Tuple2(T1 t1, T2 t2) {
        this.t1 = t1;
        this.t2 = t2;
    }

    public T1 getT1() {
        return t1;
    }

    public T2 getT2() {
        return t2;
    }
}
