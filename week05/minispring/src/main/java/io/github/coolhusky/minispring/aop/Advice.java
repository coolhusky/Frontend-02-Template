package io.github.coolhusky.minispring.aop;

public interface Advice {

    Pointcut getPointcut();

    default boolean supportProceeding() {
        return false;
    }

}
