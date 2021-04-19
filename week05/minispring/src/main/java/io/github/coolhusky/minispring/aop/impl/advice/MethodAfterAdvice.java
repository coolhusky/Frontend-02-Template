package io.github.coolhusky.minispring.aop.impl.advice;

import io.github.coolhusky.minispring.aop.AfterAdvice;
import io.github.coolhusky.minispring.aop.AspectInstanceFactory;
import io.github.coolhusky.minispring.aop.Pointcut;

import java.lang.reflect.Method;

/**
 * @author jcwang
 */
public class MethodAfterAdvice extends AbstractAdvice implements AfterAdvice {

    public MethodAfterAdvice(Method adviceMethod, Pointcut pointcut, AspectInstanceFactory aspectInstanceFactory) {
        super(adviceMethod, pointcut, aspectInstanceFactory);
    }


    @Override
    public void after(Object target, Method method, Object[] args) throws Throwable {
        invokeAdviceMethod();
    }
}
