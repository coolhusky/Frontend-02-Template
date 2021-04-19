package io.github.coolhusky.minispring.aop.impl;

import io.github.coolhusky.minispring.aop.support.AopUtils;
import io.github.coolhusky.minispring.beans.BeanAltered;
import io.github.coolhusky.minispring.beans.impl.BeanAlteredImpl;
import io.github.coolhusky.minispring.annotations.Aspect;
import io.github.coolhusky.minispring.aop.AspectRegistry;
import io.github.coolhusky.minispring.beans.BeanFactory;
import io.github.coolhusky.minispring.beans.BeanFactoryAware;
import io.github.coolhusky.minispring.beans.BeanPostProcessor;
import io.github.coolhusky.minispring.exception.MiniSpringException;
import io.github.coolhusky.minispring.utils.ClassUtils;

/**
 * @author jcwang
 */
public class JdkDynamicAopProxyCreator  implements BeanPostProcessor, BeanFactoryAware {
    private final AspectRegistry aspectRegistry = AnnotatedAspectRegistry.getInstance();
    private BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public void beforeInstantiation(Class<?> beanClass, String beanName) throws MiniSpringException {
        // register aspect info
        if (ClassUtils.isAnnotated(beanClass, Aspect.class)) {
            this.aspectRegistry.registerAspectInfo(beanClass);
        }
    }

    @Override
    public BeanAltered postProcessBeforeInitialization(Object bean, String beanName) throws MiniSpringException {
        // register aspect beans
        if (aspectRegistry.isAspect(bean)) {
            aspectRegistry.registerAspect(bean, beanName);
        }
        return new BeanAlteredImpl(bean);
    }

    @Override
    public BeanAltered postProcessAfterInitialization(Object bean, String beanName) throws MiniSpringException {
        // 判断一个bean是否被切
        if (AopUtils.needProxy(bean.getClass())) {
            Object proxy = createProxy(bean, beanName);
            return new BeanAlteredImpl(bean, proxy, true);
        }
        return new BeanAlteredImpl(bean);
    }

    private Object createProxy(Object bean, String beanName) {
        AdvisedSupport config = new AdvisedSupport(bean, beanFactory.getClassLoader());
        return new JdkDynamicAopProxy(config).getProxy();
    }
}
