package io.github.coolhusky.minispring.aop;

public interface MethodInterceptor {

    Object invoke(MethodInvocation mi) throws Throwable;
}
