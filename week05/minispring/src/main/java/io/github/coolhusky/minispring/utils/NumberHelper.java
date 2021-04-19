package io.github.coolhusky.minispring.utils;

import java.util.Optional;

/**
 * @author jcwang
 */
public class NumberHelper {
    private NumberHelper() {
    }

    public static int toPrimitive(Integer value) {
        return toPrimitive(value, 0);
    }

    public static int toPrimitive(Integer value, int defaultValue) {
        return Optional.ofNullable(value).orElse(defaultValue);
    }

    public static long toPrimitive(Long value) {
        return toPrimitive(value, 0L);
    }

    public static long toPrimitive(Long value, long defaultValue) {
        return Optional.ofNullable(value).orElse(defaultValue);
    }
}
