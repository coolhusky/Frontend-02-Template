package io.github.coolhusky.minispring.aop.impl;

import io.github.coolhusky.minispring.aop.MethodInterceptor;
import io.github.coolhusky.minispring.aop.Advice;
import io.github.coolhusky.minispring.aop.Advisor;

/**
 * @author jcwang
 */
public interface AdvisorAdapter {

    boolean supportAdvice(Advice advice);

    MethodInterceptor getInterceptor(Advisor advisor);
}
