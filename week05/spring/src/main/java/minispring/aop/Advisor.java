package minispring.aop;

import minispring.support.Ordered;

/**
 * wrap advice
 * @author jcwang
 */
public interface Advisor extends Ordered {
    AspectInformation getAspectInfo();

    Pointcut getPointcut();

    Advice getAdvice();
}
