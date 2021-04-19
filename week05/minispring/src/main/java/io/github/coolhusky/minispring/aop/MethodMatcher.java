package io.github.coolhusky.minispring.aop;

import java.lang.reflect.Method;

/**
 * @author jcwang
 */
public interface MethodMatcher {

    boolean match(Class<?> targetClass, Method method);
}
