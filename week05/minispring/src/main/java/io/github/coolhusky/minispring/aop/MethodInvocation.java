package io.github.coolhusky.minispring.aop;

import java.lang.reflect.Method;

public interface MethodInvocation extends JoinPoint {
    Object getProxy();

    Object[] getArguments();

    Method getMethod();

    MethodInterceptor getCurrentInterceptor();
}
