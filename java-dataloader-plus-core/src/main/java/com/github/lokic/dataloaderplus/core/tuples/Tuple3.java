package com.github.lokic.dataloaderplus.core.tuples;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@ToString
@EqualsAndHashCode
public class Tuple3<T1, T2, T3> implements Tuple, Serializable {

    private static final long serialVersionUID = -7153679497399532428L;

    private final T1 t1;
    private final T2 t2;
    private final T3 t3;

    Tuple3(T1 t1, T2 t2, T3 t3) {
        this.t1 = t1;
        this.t2 = t2;
        this.t3 = t3;
    }

    public T1 getT1() {
        return t1;
    }

    public T2 getT2() {
        return t2;
    }

    public T3 getT3() {
        return t3;
    }
}
