package io.github.coolhusky.minispring.aop.impl;

import io.github.coolhusky.minispring.aop.*;
import io.github.coolhusky.minispring.utils.ArrayUtils;
import io.github.coolhusky.minispring.utils.ClassUtils;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author jcwang
 */
public class DefaultAdvisorChainFactory implements AdvisorChainFactory {

    @Override
    public List<MethodInterceptor> getChainedInterceptors(Advised advised, Method method, Class<?> targetClass) {
        Advisor[] advisors = advised.getAdvisors();
        if (ArrayUtils.isNullOrEmpty(advisors)) {
            return Collections.emptyList();
        }
        AdvisorRegistry advisorRegistry = DefaultAdvisorRegistry.getInstance();
        return Stream.of(advisors)
                .filter(advisor ->
                        ClassUtils.isAssignedFrom(targetClass, advisor.getPointcut().getTargetClass())
                                && method.equals( advisor.getPointcut().getTargetMethod()))
                .flatMap(advisor -> Stream.of(advisorRegistry.getInterceptors(advisor)))
                .collect(Collectors.toList());
    }
}
