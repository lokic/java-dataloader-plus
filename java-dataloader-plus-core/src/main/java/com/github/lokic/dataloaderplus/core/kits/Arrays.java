package com.github.lokic.dataloaderplus.core.kits;

public class Arrays {


    public static Object[] remove(Object[] array, int index) {
        final int length = array.length;
        if (index < 0 || index >= length) {
            return array;
        }
        final Object[] result = new Object[length - 1];
        System.arraycopy(array, 0, result, 0, index);
        if (index < length - 1) {
            System.arraycopy(array, index + 1, result, index, length - index - 1);
        }
        return result;
    }

}
