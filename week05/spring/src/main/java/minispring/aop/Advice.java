package minispring.aop;

public interface Advice {

    Pointcut getPointcut();

    default boolean supportProceeding() {
        return false;
    }

}
