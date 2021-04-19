package minispring.aop;

public interface JoinPoint {

    Object proceed() throws Throwable;

    /**
     * target
     *
     * @return
     */
    Object getThis();
}
