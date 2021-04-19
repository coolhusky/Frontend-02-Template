package minispring.aop.impl.advice;

import minispring.annotations.After;
import minispring.annotations.Around;
import minispring.annotations.Before;
import minispring.annotations.Pointcut;
import minispring.aop.Advisor;
import minispring.aop.Aspect;
import minispring.aop.AspectInstanceFactory;
import minispring.exception.MiniSpringRuntimeException;
import minispring.utils.AssertUtils;
import minispring.utils.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author jcwang
 */
public class AnnotatedAspect implements Aspect {
    private final Predicate<? super Method> ADVICE_FILTER = m ->
            m.isAnnotationPresent(Before.class)
                    || m.isAnnotationPresent(After.class)
                    || m.isAnnotationPresent(Around.class);

    private String name;
    private final Class<?> aspectClass;
    private final int order;
    private AspectInstanceFactory aspectInstanceFactory;
    private final List<Advisor> advisors = new ArrayList<>(16);
    private final Map<String, minispring.aop.Pointcut> POINT_CUT_CACHE = new ConcurrentHashMap<>(16);

    public AnnotatedAspect(String name, Class<?> aspectClass) {
        this.aspectClass = aspectClass;
        this.name = name;
        AssertUtils.isTrue(() -> aspectClass.isAnnotationPresent(minispring.annotations.Aspect.class),
                new MiniSpringRuntimeException("must be annotated as @minispring.annotations.Aspect"));
        minispring.annotations.Aspect annotation = aspectClass.getAnnotation(minispring.annotations.Aspect.class);
        this.order = annotation.order();
//        retrieveAdvisors();
    }

    private void retrieveAdvisors() {
        Method[] methods = this.aspectClass.getDeclaredMethods();
        // find pointcut
        Stream.of(methods)
                .filter(m -> m.isAnnotationPresent(Pointcut.class))
                .map(m -> {
                    Pointcut pc = m.getAnnotation(Pointcut.class);
                    Class<?> targetClass = pc.targetClass();
                    Method targetMethod = ReflectionUtils.getMethod(targetClass, pc.methodName(), pc.args());
                    return new AnnotatedPointcut(pointcutCacheKey(m), targetClass, targetMethod);
                }).forEach(pc -> POINT_CUT_CACHE.put(pc.getName(), pc));
        // find advice
        List<AnnotatedAdvisor> advisors = Stream.of(methods)
                .filter(ADVICE_FILTER)
                .map(m -> {
                    if (m.isAnnotationPresent(Before.class)) {
                        Before annotation = m.getAnnotation(Before.class);
                        return Optional.ofNullable(POINT_CUT_CACHE.get(annotation.pointcut()))
                                .map(pc -> new MethodBeforeAdvice(m, pc, aspectInstanceFactory))
                                .orElseThrow(() -> new MiniSpringRuntimeException("pointcut not found: " + annotation.pointcut()));
                    }
                    if (m.isAnnotationPresent(After.class)) {
                        After annotation = m.getAnnotation(After.class);
                        return Optional.ofNullable(POINT_CUT_CACHE.get(annotation.pointcut()))
                                .map(pc -> new MethodAfterAdvice(m, pc, aspectInstanceFactory))
                                .orElseThrow(() -> new MiniSpringRuntimeException("pointcut not found: " + annotation.pointcut()));
                    }

                    if (m.isAnnotationPresent(Around.class)) {
                        Around annotation = m.getAnnotation(Around.class);
                        return Optional.ofNullable(POINT_CUT_CACHE.get(annotation.pointcut()))
                                .map(pc -> new MethodAroundAdvice(m, pc, aspectInstanceFactory))
                                .orElseThrow(() -> new MiniSpringRuntimeException("pointcut not found: " + annotation.pointcut()));
                    }
                    return null;
                }).filter(Objects::nonNull)
                .map(advice ->
                        new AnnotatedAdvisor( advice, this))
                .collect(Collectors.toList());
        this.advisors.addAll(advisors);
    }

    private String pointcutCacheKey(Method pointcutMethod) {
        return String.format("%s()", pointcutMethod.getName());
    }


    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Class<?> getAspectClass() {
        return this.aspectClass;
    }

    @Override
    public int getOrder() {
        return this.order;
    }

    @Override
    public AspectInstanceFactory getAspectInstanceFactory() {
        return this.aspectInstanceFactory;
    }

    @Override
    public void setAspectInstanceFactory(AspectInstanceFactory factory) {
        this.aspectInstanceFactory = factory;
        retrieveAdvisors();
    }

    @Override
    public void setAspectName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnnotatedAspect that = (AnnotatedAspect) o;
        return Objects.equals(aspectClass, that.aspectClass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(aspectClass);
    }

    @Override
    public List<Advisor> getAdvisors() {
        return this.advisors;
    }


}
