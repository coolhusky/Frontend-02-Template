package io.github.coolhusky.minispring.aop;

import java.util.List;

/**
 * @author jcwang
 */
public interface AdvisorRegistry {


    List<Advisor> getAdvisors(Class<?> targetClass);

    MethodInterceptor[] getInterceptors(Advisor advisor);
}
