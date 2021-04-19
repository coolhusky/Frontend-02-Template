package io.github.coolhusky.minispring.aop.impl;

import com.google.common.collect.ImmutableList;
import io.github.coolhusky.minispring.aop.AspectAware;
import io.github.coolhusky.minispring.aop.AspectInformation;
import io.github.coolhusky.minispring.aop.Aspect;
import io.github.coolhusky.minispring.aop.AspectRegistry;
import io.github.coolhusky.minispring.aop.impl.advice.AnnotatedAspect;
import io.github.coolhusky.minispring.aop.impl.advice.DefaultAspectInstanceFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author jcwang
 */
public final class AnnotatedAspectRegistry implements AspectRegistry {
    private static final AnnotatedAspectRegistry INSTANCE = new AnnotatedAspectRegistry();
    private final Map<Class<?>, AspectInformation> aspectInfoCache = new ConcurrentHashMap<>(16);
    private final List<AspectAware> aspectAwares;

    private AnnotatedAspectRegistry() {
        this.aspectAwares = ImmutableList.of(DefaultAdvisorRegistry.getInstance());
    }

    public static AnnotatedAspectRegistry getInstance() {
        return INSTANCE;
    }

    @Override
    public void registerAspectInfo(Class<?> aspectClass) {
        if (aspectClass == null) {
            return;
        }
        String aspectName = aspectClass.getName();
        AspectInformation aspectInfo = new AnnotatedAspect(aspectName, aspectClass);
        this.aspectInfoCache.put(aspectClass, aspectInfo);
        // call awares
        this.aspectAwares.forEach(aware -> aware.onRegisterInfo(aspectInfo));
    }


    @Override
    public boolean isAspect(Object candidateAspect) {
        if (candidateAspect == null) {
            return false;
        }

        Class<?> clazz = candidateAspect.getClass();
        return this.aspectInfoCache.containsKey(clazz);
    }

    @Override
    public boolean registerAspect(Object candidateAspect, String candidateAspectName) {
        if (!isAspect(candidateAspect)) {
            return false;
        }
        Class<?> aspectClass = candidateAspect.getClass();
        AspectInformation aspectInfo = this.aspectInfoCache.get(aspectClass);
        if (aspectInfo instanceof Aspect) {
            Aspect aspect = (Aspect) aspectInfo;
            aspect.setAspectName(candidateAspectName);
            aspect.setAspectInstanceFactory(new DefaultAspectInstanceFactory(candidateAspect));
            // invoke awares
            this.aspectAwares.forEach(aware -> aware.onRegister(aspect));
            return true;
        }
        return false;
    }


}
