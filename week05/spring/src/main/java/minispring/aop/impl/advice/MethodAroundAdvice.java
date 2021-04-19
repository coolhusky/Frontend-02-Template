package minispring.aop.impl.advice;

import minispring.aop.*;
import minispring.aop.impl.MethodInvocationProceedingJoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.weaver.tools.JoinPointMatch;
import org.springframework.aop.ProxyMethodInvocation;

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
