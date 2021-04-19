package io.github.coolhusky.minispring.aop.impl.advice;

import io.github.coolhusky.minispring.aop.MethodInterceptor;
import io.github.coolhusky.minispring.aop.impl.AdvisorAdapter;
import io.github.coolhusky.minispring.aop.Advice;
import io.github.coolhusky.minispring.aop.Advisor;
import io.github.coolhusky.minispring.aop.BeforeAdvice;

/**
 * @author jcwang
 */
public class MethodBeforeAdvisorAdapter implements AdvisorAdapter {
    @Override
    public boolean supportAdvice(Advice advice) {
        return (advice instanceof BeforeAdvice);
    }

    @Override
    public MethodInterceptor getInterceptor(Advisor advisor) {
        BeforeAdvice beforeAdvice = (BeforeAdvice) advisor.getAdvice();
        return new MethodBeforeAdviceInterceptor(beforeAdvice);
    }
}
