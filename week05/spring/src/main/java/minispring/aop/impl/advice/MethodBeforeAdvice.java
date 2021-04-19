package minispring.aop.impl.advice;

import minispring.aop.AspectInstanceFactory;
import minispring.aop.BeforeAdvice;
import minispring.aop.Pointcut;

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
