package io.github.coolhusky.minispring.aop.impl.advice;

import io.github.coolhusky.minispring.aop.AspectInstanceFactory;
import io.github.coolhusky.minispring.aop.BeforeAdvice;
import io.github.coolhusky.minispring.aop.Pointcut;

import java.lang.reflect.Method;

/**
 * @author jcwang
 */
public class MethodBeforeAdvice extends AbstractAdvice implements BeforeAdvice {
    public MethodBeforeAdvice(Method adviceMethod, Pointcut pointcut, AspectInstanceFactory aspectInstanceFactory) {
        super(adviceMethod, pointcut, aspectInstanceFactory);
    }

    @Override
    public void before(Object target, Method method, Object[] args) throws Throwable {
        invokeAdviceMethod();
    }
}
