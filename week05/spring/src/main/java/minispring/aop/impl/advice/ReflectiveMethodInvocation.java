package minispring.aop.impl.advice;

import minispring.aop.MethodInterceptor;
import minispring.aop.MethodInvocation;
import minispring.utils.ReflectionUtils;
import org.apache.commons.collections.CollectionUtils;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author jcwang
 */
public class ReflectiveMethodInvocation implements MethodInvocation {
    private final Object proxy;
    private final Object target;
    private final Method method;
    private final Object[] arguments;
    private final List<MethodInterceptor> methodInterceptors;
    private int currentIndex = -1;

    public ReflectiveMethodInvocation(Object proxy,
                                      Object target,
                                      Method method,
                                      Object[] arguments,
                                      List<MethodInterceptor> methodInterceptors) {
        this.proxy = proxy;
        this.target = target;
        this.method = method;
        this.arguments = arguments;
        this.methodInterceptors = methodInterceptors;
    }

    @Override
    public Object getProxy() {
        return this.proxy;
    }

    @Override
    public Object[] getArguments() {
        return this.arguments;
    }

    @Override
    public Method getMethod() {
        return this.method;
    }

    @Override
    public MethodInterceptor getCurrentInterceptor() {
        if (currentIndex == -1 || CollectionUtils.isEmpty(methodInterceptors)) {
            return null;
        }
        return this.methodInterceptors.get(currentIndex);
    }

    @Override
    public Object getThis() {
        return this.target;
    }


    /**
     * 责任链模式调用多层interceptors
     *
     * @return
     * @throws Throwable
     */
    @Override
    public Object proceed() throws Throwable {
        if (currentIndex == this.methodInterceptors.size() - 1) {
            return invokeJoinPoint();
        }
        return this.methodInterceptors.get(++currentIndex).invoke(this);
    }

    private Object invokeJoinPoint() throws Throwable {
        return ReflectionUtils.invoke(this.target, this.method, this.arguments);
    }
}
