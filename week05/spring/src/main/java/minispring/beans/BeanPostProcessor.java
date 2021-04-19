package minispring.beans;

import minispring.beans.impl.BeanAlteredImpl;
import minispring.exception.MiniSpringException;

/**
 * @author jcwang
 */
public interface BeanPostProcessor {

    default void beforeInstantiation(Class<?> beanClass, String beanName) throws MiniSpringException {

    }

    default BeanAltered postProcessBeforeInitialization(Object bean, String beanName) throws MiniSpringException {
        return new BeanAlteredImpl(bean);
    }

    default BeanAltered postProcessAfterInitialization(Object bean, String beanName) throws MiniSpringException {
        return new BeanAlteredImpl(bean);
    }
}
