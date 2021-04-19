package minispring.aop;

import java.lang.reflect.Method;

/**
 * @author jcwang
 */
public interface Advised {

    Object getTarget();

    ClassLoader getClassLoader();

    Class<?> getTargetClass();

    Advisor[] getAdvisors();

}
