package minispring.aop.impl.advice;

import lombok.AllArgsConstructor;
import minispring.aop.BeforeAdvice;
import minispring.aop.MethodInterceptor;
import minispring.aop.MethodInvocation;

/**
 * @author jcwang
 */
@AllArgsConstructor
public class MethodBeforeAdviceInterceptor implements MethodInterceptor {
    private final BeforeAdvice advice;

    @Override
    public Object invoke(MethodInvocation mi) throws Throwable {
        try {
//            System.out.println(mi.getCurrentInterceptor().getClass().getSimpleName() + "=== before 入栈 ===");
            this.advice.before(mi.getThis(), mi.getMethod(), mi.getArguments());
            return mi.proceed();
        } finally {
//            System.out.println(mi.getCurrentInterceptor().getClass().getSimpleName() + "=== before 出栈 ===");
        }

    }
}
