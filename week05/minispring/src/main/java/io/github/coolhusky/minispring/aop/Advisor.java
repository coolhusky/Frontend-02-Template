package io.github.coolhusky.minispring.aop;

import io.github.coolhusky.minispring.support.Ordered;

/**
 * wrap advice
 * @author jcwang
 */
public interface Advisor extends Ordered {
    AspectInformation getAspectInfo();

    Pointcut getPointcut();

    Advice getAdvice();
}
