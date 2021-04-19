package io.github.coolhusky.minispring.aop;

public interface JoinPoint {

    Object proceed() throws Throwable;

    /**
     * target
     *
     * @return
     */
    Object getThis();
}
