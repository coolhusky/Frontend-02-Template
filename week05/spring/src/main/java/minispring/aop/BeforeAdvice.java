package minispring.aop;

import java.lang.reflect.Method;

public interface BeforeAdvice extends Advice {

    void before(Object target, Method method, Object[] args) throws Throwable;
}
