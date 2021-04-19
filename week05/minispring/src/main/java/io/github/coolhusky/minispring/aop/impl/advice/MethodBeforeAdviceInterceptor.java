package io.github.coolhusky.minispring.aop.impl.advice;

import io.github.coolhusky.minispring.aop.BeforeAdvice;
import io.github.coolhusky.minispring.aop.MethodInterceptor;
import io.github.coolhusky.minispring.aop.MethodInvocation;
import lombok.AllArgsConstructor;

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
