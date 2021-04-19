package io.github.coolhusky.minispring.aop.impl;

import io.github.coolhusky.minispring.aop.AopProxy;
import io.github.coolhusky.minispring.aop.MethodInterceptor;
import io.github.coolhusky.minispring.aop.impl.advice.ReflectiveMethodInvocation;
import io.github.coolhusky.minispring.exception.MiniSpringRuntimeException;
import io.github.coolhusky.minispring.utils.ArrayUtils;
import io.github.coolhusky.minispring.utils.ReflectionUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 * @author jcwang
 */
public class JdkDynamicAopProxy implements AopProxy, InvocationHandler {
    private final Object target;
    private final Class<?> targetClass;
    private final ClassLoader classLoader;
    private final Class<?>[] interfaces;
    private final AdvisedSupport advised;

    public JdkDynamicAopProxy(AdvisedSupport advised) {
        this.advised = advised;
        this.target = advised.getTarget();
        this.classLoader = advised.getClassLoader();
        if (ArrayUtils.isNullOrEmpty(target.getClass().getInterfaces())) {
            throw new MiniSpringRuntimeException("not support");
        }
        this.targetClass = target.getClass();
        this.interfaces = targetClass.getInterfaces();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 需要拦截的方法调用interceptor chain
        List<MethodInterceptor> chainedInterceptors = this.advised.getChainedInterceptors(method, this.targetClass);
        if (chainedInterceptors == AdvisedSupport.NO_PROXY) {
            // 否则调用原方法
            return ReflectionUtils.invoke(target, method, args);
        }
        return new ReflectiveMethodInvocation(proxy, target, method, args, chainedInterceptors).proceed();
    }

    @Override
    public Object getProxy() {
        return Proxy.newProxyInstance(this.classLoader, this.interfaces, this);
    }
}
