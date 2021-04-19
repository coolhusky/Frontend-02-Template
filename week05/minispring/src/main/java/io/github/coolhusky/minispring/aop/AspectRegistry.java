package io.github.coolhusky.minispring.aop;

/**
 * @author jcwang
 */
public interface AspectRegistry {

    void registerAspectInfo(Class<?> aspectClass);

    boolean isAspect(Object candidateAspect);

    boolean registerAspect(Object candidateAspect, String candidateAspectName);
}
