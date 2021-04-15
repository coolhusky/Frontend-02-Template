package minispring.beans;

import minispring.exception.MiniSpringException;

/**
 * @author jcwang
 */
public interface BeanPostProcessor {

    default Object postProcessBeforeInitialization(Object bean, String beanName) throws MiniSpringException {
        return bean;
    }

    default Object postProcessAfterInitialization(Object bean, String beanName) throws MiniSpringException {
        return bean;
    }
}
