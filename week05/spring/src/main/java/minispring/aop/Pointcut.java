package minispring.aop;

import java.lang.reflect.Method;

public interface Pointcut {

    String getName();

    Class<?> getTargetClass();

    Method getTargetMethod();
}
