package me.lokic.dataloaderplus.core.tuples;

public interface Tuple {

    static Tuple fromArray(Object[] list) {
        if (list == null || list.length < 2) {
            throw new IllegalArgumentException("null or too small array, need between 2 and 3 values");
        }
        switch (list.length) {
            case 2:
                return of(list[0], list[1]);
            case 3:
                return of(list[0], list[1], list[2]);
            default:
                throw new IllegalArgumentException("too many arguments (" + list.length + "), need between 2 and 3 values");
        }
    }


    static <T1, T2> Tuple2<T1, T2> of(T1 t1, T2 t2) {
        return new Tuple2<>(t1, t2);
    }

    static <T1, T2, T3> Tuple3<T1, T2, T3> of(T1 t1, T2 t2, T3 t3) {
        return new Tuple3<>(t1, t2, t3);
    }

}
