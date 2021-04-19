package io.github.coolhusky.minispring.aop.impl.advice;

import io.github.coolhusky.minispring.aop.MethodInterceptor;
import io.github.coolhusky.minispring.aop.impl.AdvisorAdapter;
import io.github.coolhusky.minispring.aop.Advice;
import io.github.coolhusky.minispring.aop.Advisor;
import io.github.coolhusky.minispring.aop.AfterAdvice;

/**
 * @author jcwang
 */
public class MethodAfterAdvisorAdapter implements AdvisorAdapter {
    @Override
    public boolean supportAdvice(Advice advice) {
        return (advice instanceof AfterAdvice);
    }

    @Override
    public MethodInterceptor getInterceptor(Advisor advisor) {
        AfterAdvice advice = (AfterAdvice) advisor.getAdvice();
        return new MethodAfterAdviceInterceptor(advice);
    }
}
