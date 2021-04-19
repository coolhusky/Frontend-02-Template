package io.github.coolhusky.minispring.aop;

/**
 * @author jcwang
 */
public interface Advised {

    Object getTarget();

    ClassLoader getClassLoader();

    Class<?> getTargetClass();

    Advisor[] getAdvisors();

}
