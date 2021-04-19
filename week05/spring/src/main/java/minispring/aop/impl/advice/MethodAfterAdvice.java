package minispring.aop.impl.advice;

import minispring.aop.*;

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
