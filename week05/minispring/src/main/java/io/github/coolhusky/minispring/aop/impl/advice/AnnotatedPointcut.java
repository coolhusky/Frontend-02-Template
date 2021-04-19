package io.github.coolhusky.minispring.aop.impl.advice;

import io.github.coolhusky.minispring.aop.Pointcut;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author jcwang
 */

public class AnnotatedPointcut implements Pointcut {
    private final String name;
    private final Class<?> targetClass;
    private final Method targetMethod;

    public AnnotatedPointcut(String name, Class<?> targetClass, Method targetMethod) {
        this.name = name;
        this.targetClass = targetClass;
        this.targetMethod = targetMethod;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Class<?> getTargetClass() {
        return this.targetClass;
    }

    @Override
    public Method getTargetMethod() {
        return this.targetMethod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnnotatedPointcut that = (AnnotatedPointcut) o;
        return Objects.equals(targetClass, that.targetClass) && Objects.equals(targetMethod, that.targetMethod);
    }

    @Override
    public int hashCode() {
        return Objects.hash(targetClass, targetMethod);
    }
}
