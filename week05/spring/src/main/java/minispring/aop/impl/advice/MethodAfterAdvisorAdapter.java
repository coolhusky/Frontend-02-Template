package minispring.aop.impl.advice;

import minispring.aop.Advice;
import minispring.aop.Advisor;
import minispring.aop.AfterAdvice;
import minispring.aop.MethodInterceptor;
import minispring.aop.impl.AdvisorAdapter;

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
