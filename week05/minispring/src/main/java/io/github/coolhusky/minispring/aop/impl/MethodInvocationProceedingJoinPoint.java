package io.github.coolhusky.minispring.aop.impl;

import io.github.coolhusky.minispring.aop.JoinPoint;
import io.github.coolhusky.minispring.aop.MethodInvocation;

/**
 * @author jcwang
 */
public class MethodInvocationProceedingJoinPoint implements JoinPoint {
    private MethodInvocation methodInvocation;

    public MethodInvocationProceedingJoinPoint(MethodInvocation methodInvocation) {
        this.methodInvocation = methodInvocation;
    }

    @Override
    public Object proceed() throws Throwable {
        return this.methodInvocation.proceed();
    }

    @Override
    public Object getThis() {
        return this.methodInvocation.getProxy();
    }
}
