package io.github.coolhusky.minispring.aop.support;

import io.github.coolhusky.minispring.aop.AdvisorRegistry;
import io.github.coolhusky.minispring.aop.impl.DefaultAdvisorRegistry;
import io.github.coolhusky.minispring.beans.BeanDefinition;
import io.github.coolhusky.minispring.beans.BeanFactory;
import org.apache.commons.collections.CollectionUtils;

/**
 * @author jcwang
 */
public class AopUtils {
    private static final String ORIGINAL_TARGET_CLASS = "originalTargetClass";

    private AopUtils() {}

    public static boolean needProxy(Class<?> targetClass) {
        AdvisorRegistry advisorRegistry = DefaultAdvisorRegistry.getInstance();
        return CollectionUtils.isNotEmpty(advisorRegistry.getAdvisors(targetClass));
    }

    public static void exposeOriginalTargetClass(BeanFactory beanFactory, Class<?> targetClass) {
        BeanDefinition bd = beanFactory.getBeanDefinition(targetClass);
        if (bd != null) {
            bd.setAttribute(ORIGINAL_TARGET_CLASS, targetClass);
        }
    }
}
