package minispring.beans;

import minispring.exception.MiniSpringException;

/**
 * @author jinchaowang
 */
public interface BeanFactory {

    <T> T getBean(Class<T> requiredType) throws MiniSpringException;
}
