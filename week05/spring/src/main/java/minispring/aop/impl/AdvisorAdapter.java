package minispring.aop.impl;

import minispring.aop.Advice;
import minispring.aop.Advisor;
import minispring.aop.MethodInterceptor;

/**
 * @author jcwang
 */
public interface AdvisorAdapter {

    boolean supportAdvice(Advice advice);

    MethodInterceptor getInterceptor(Advisor advisor);
}
