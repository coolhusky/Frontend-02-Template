package com.github.coolhusky.gateway.support;

import java.util.Map;

public class MapUtils {
    private MapUtils(){}

    public static boolean isEmpty(Map<?,?> map) {
        return map == null || map.isEmpty();
    }

    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }
}
