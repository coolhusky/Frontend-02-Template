package minispring.aop.impl.advice;

import lombok.AllArgsConstructor;
import minispring.aop.AfterAdvice;
import minispring.aop.MethodInterceptor;
import minispring.aop.MethodInvocation;

/**
 * @author jcwang
 */
@AllArgsConstructor
public class MethodAfterAdviceInterceptor implements MethodInterceptor {
    private final AfterAdvice advice;

    @Override
    public Object invoke(MethodInvocation mi) throws Throwable {
        try {
//            System.out.println(mi.getCurrentInterceptor().getClass().getSimpleName() + "=== after 入栈 ===");
            return mi.proceed();
        } finally {
            this.advice.after(mi.getThis(), mi.getMethod(), mi.getArguments());
//            System.out.println(mi.getCurrentInterceptor().getClass().getSimpleName() + "=== after 出栈 ===");
        }
    }
}
