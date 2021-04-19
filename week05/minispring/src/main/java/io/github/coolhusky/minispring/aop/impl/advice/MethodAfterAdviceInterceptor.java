package io.github.coolhusky.minispring.aop.impl.advice;

import io.github.coolhusky.minispring.aop.AfterAdvice;
import io.github.coolhusky.minispring.aop.MethodInterceptor;
import io.github.coolhusky.minispring.aop.MethodInvocation;
import lombok.AllArgsConstructor;

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
