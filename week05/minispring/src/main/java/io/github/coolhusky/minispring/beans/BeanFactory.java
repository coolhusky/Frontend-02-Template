package io.github.coolhusky.minispring.beans;

import io.github.coolhusky.minispring.exception.MiniSpringException;

/**
 * @author jinchaowang
 */
public interface BeanFactory {

    <T> T getBean(Class<T> requiredType) throws MiniSpringException;
    <T> T getBean(String beanName, Class<?> requiredType) throws MiniSpringException;
    ClassLoader getClassLoader();
    BeanDefinition getBeanDefinition(Class<?> beanClass);
}
