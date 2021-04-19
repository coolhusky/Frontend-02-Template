package io.github.coolhusky.minispring.aop;

import java.lang.reflect.Method;

public interface AfterAdvice extends Advice {

    void after(Object target, Method method, Object[] args) throws Throwable;
}
