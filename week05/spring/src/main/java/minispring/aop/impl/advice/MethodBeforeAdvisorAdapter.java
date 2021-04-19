package minispring.aop.impl.advice;

import minispring.aop.Advice;
import minispring.aop.Advisor;
import minispring.aop.BeforeAdvice;
import minispring.aop.MethodInterceptor;
import minispring.aop.impl.AdvisorAdapter;

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
