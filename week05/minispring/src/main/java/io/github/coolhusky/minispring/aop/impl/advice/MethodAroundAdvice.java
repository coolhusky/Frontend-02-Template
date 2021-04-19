package io.github.coolhusky.minispring.aop.impl.advice;

import io.github.coolhusky.minispring.aop.AspectInstanceFactory;
import io.github.coolhusky.minispring.aop.MethodInterceptor;
import io.github.coolhusky.minispring.aop.MethodInvocation;
import io.github.coolhusky.minispring.aop.Pointcut;
import io.github.coolhusky.minispring.aop.impl.MethodInvocationProceedingJoinPoint;


import java.lang.reflect.Method;

/**
 * @author jcwang
 */
public class MethodAroundAdvice extends AbstractAdvice implements MethodInterceptor {

    public MethodAroundAdvice(Method adviceMethod, Pointcut pointcut, AspectInstanceFactory aspectInstanceFactory) {
        super(adviceMethod, pointcut, aspectInstanceFactory);
    }

    @Override
    public Object invoke(MethodInvocation mi) throws Throwable {
        try {
//            System.out.println(mi.getCurrentInterceptor().getClass().getSimpleName() + "==== around 入栈 ====");
            MethodInvocationProceedingJoinPoint jp = new MethodInvocationProceedingJoinPoint(mi);
            return invokeAdviceMethod(jp);
        } finally {
//            System.out.println(mi.getCurrentInterceptor().getClass().getSimpleName() + "==== around 出栈 ====");
        }



    }

    @Override
    public boolean supportProceeding() {
        return true;
    }
}
