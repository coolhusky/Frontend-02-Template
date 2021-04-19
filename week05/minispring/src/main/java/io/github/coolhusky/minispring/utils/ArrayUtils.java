package io.github.coolhusky.minispring.utils;

/**
 * @author jcwang
 */
public class ArrayUtils {
    private ArrayUtils() {
    }

    public static boolean isNullOrEmpty(Object[] arr) {
        return arr == null || arr.length == 0;
    }
}
