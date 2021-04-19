package minispring.beans;

import minispring.exception.MiniSpringException;

/**
 * @author jinchaowang
 */
public interface BeanFactory {

    <T> T getBean(Class<T> requiredType) throws MiniSpringException;
    <T> T getBean(String beanName, Class<?> requiredType) throws MiniSpringException;
    ClassLoader getClassLoader();
    BeanDefinition getBeanDefinition(Class<?> beanClass);
}
