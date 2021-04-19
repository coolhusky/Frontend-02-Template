package io.github.coolhusky.minispring.aop.impl;

import com.google.common.collect.ImmutableList;
import io.github.coolhusky.minispring.aop.*;
import io.github.coolhusky.minispring.aop.impl.advice.MethodBeforeAdvisorAdapter;
import io.github.coolhusky.minispring.aop.impl.advice.MethodAfterAdvisorAdapter;
import io.github.coolhusky.minispring.utils.ClassUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author jcwang
 */
public final class DefaultAdvisorRegistry implements AdvisorRegistry, AspectAware {
    private static final DefaultAdvisorRegistry INSTANCE = new DefaultAdvisorRegistry();

    private final Map<Class<?>, List<Advisor>> advisorCache = new ConcurrentHashMap<>(16);
    private final List<AdvisorAdapter> adapters = ImmutableList.of(
            new MethodBeforeAdvisorAdapter(),
            new MethodAfterAdvisorAdapter()
    );
    private DefaultAdvisorRegistry(){
    }


    public static DefaultAdvisorRegistry getInstance() {
        return INSTANCE;
    }

    public boolean registerAdvisor(Advisor advisor) {
        Class<?> targetClass = advisor.getPointcut().getTargetClass();
        this.advisorCache.computeIfAbsent(targetClass, cls -> new ArrayList<>());
        this.advisorCache.get(targetClass).add(advisor);
        return true;
    }


    @Override
    public List<Advisor> getAdvisors(Class<?> targetClass) {
        Set<Advisor> advisorSet = this.advisorCache.entrySet().stream()
                .filter(entry -> ClassUtils.isAssignedFrom(targetClass, entry.getKey()))
                .map(Map.Entry::getValue)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
        return ImmutableList.copyOf(advisorSet);
    }

    @Override
    public MethodInterceptor[] getInterceptors(Advisor advisor) {
        List<MethodInterceptor> interceptors = new ArrayList<>(4);
        Advice advice = advisor.getAdvice();
        if (advice instanceof MethodInterceptor) {
            interceptors.add((MethodInterceptor) advice);
        }

        this.adapters.forEach(adapter -> {
            if (adapter.supportAdvice(advice)) {
                interceptors.add(adapter.getInterceptor(advisor));
            }
        });
        return interceptors.toArray(new MethodInterceptor[0]);
    }

    @Override
    public void onRegisterInfo(AspectInformation aspectInfo) {
//        aspectInfo.getAdvisors().forEach(this::registerAdvisor);
    }

    @Override
    public void onRegister(Aspect aspect) {
       aspect.getAdvisors().forEach(this::registerAdvisor);
    }
}
