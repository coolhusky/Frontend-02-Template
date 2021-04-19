package io.github.coolhusky.minispring.aop.impl.advice;

import io.github.coolhusky.minispring.aop.Advice;
import io.github.coolhusky.minispring.aop.AspectInstanceFactory;
import io.github.coolhusky.minispring.aop.JoinPoint;
import io.github.coolhusky.minispring.aop.Pointcut;
import io.github.coolhusky.minispring.utils.ReflectionUtils;

import java.lang.reflect.Method;

/**
 * @author jcwang
 */
public abstract class AbstractAdvice implements Advice {
    private final String methodName;
    protected final Method adviceMethod;
    private final Class<?>[] parameterTypes;
    private final Pointcut pointcut;
    private final Class<?> declaringClass;
    private final AspectInstanceFactory aspectInstanceFactory;

    protected AbstractAdvice(Method adviceMethod,
                             Pointcut pointcut,
                             AspectInstanceFactory aspectInstanceFactory) {
        this.declaringClass = adviceMethod.getDeclaringClass();
        this.methodName = adviceMethod.getName();
        this.adviceMethod = adviceMethod;
        this.parameterTypes = adviceMethod.getParameterTypes();
        this.pointcut = pointcut;
        this.aspectInstanceFactory = aspectInstanceFactory;
    }

    @Override
    public Pointcut getPointcut() {
        return this.pointcut;
    }

    protected Object invokeAdviceMethod(JoinPoint jp) throws Throwable{
        return ReflectionUtils.invoke(this.aspectInstanceFactory.getAspectInstance(),
                this.adviceMethod, bindArgs(jp));
    }

    protected Object invokeAdviceMethod() throws Throwable {
        return invokeAdviceMethod(null);
    }

    private Object[] bindArgs(JoinPoint jp) {
        Object[] args = new Object[this.parameterTypes.length];

        if (jp != null) {
            args[0] = jp;
        }

        return args;
    }

}
