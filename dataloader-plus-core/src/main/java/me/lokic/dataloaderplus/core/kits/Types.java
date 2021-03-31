package me.lokic.dataloaderplus.core.kits;

public class Types {

    public static final String EMPTY = "";

    public static final char PACKAGE_SEPARATOR_CHAR = '.';

    public static String getPackageName(String className) {
        if (className == null || className.length() == 0) {
            return Types.EMPTY;
        }

        // Strip array encoding
        while (className.charAt(0) == '[') {
            className = className.substring(1);
        }
        // Strip Object type encoding
        if (className.charAt(0) == 'L' && className.charAt(className.length() - 1) == ';') {
            className = className.substring(1);
        }

        int i = className.lastIndexOf(PACKAGE_SEPARATOR_CHAR);
        if (i == -1) {
            return Types.EMPTY;
        }
        return className.substring(0, i);
    }
}
