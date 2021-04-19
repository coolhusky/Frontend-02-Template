package minispring.beans.impl;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import minispring.beans.BeanWrapper;
import minispring.exception.MiniSpringException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jcwang
 */

public class GenericBeanWrapper implements BeanWrapper {
    private final String beanName;
    private final Object bean;
    private final Class<?> beanClass;
    private final ClassLoader beanClassLoader;
    private boolean isProxy;
    private Object proxyBean;

    public GenericBeanWrapper(String beanName, Object bean, Class<?> beanClass, ClassLoader beanClassLoader) {
        this.beanName = beanName;
        this.bean = bean;
        this.beanClass = beanClass;
        this.beanClassLoader = beanClassLoader;
        this.isProxy = false;
    }

    @Override
    public boolean isProxy() {
        return isProxy;
    }


    @Override
    public void setProxy(Object proxy) {
        this.proxyBean = proxy;
        this.isProxy = true;
    }

    @Override
    public String getBeanName() {
        return this.beanName;
    }

    @Override
    public Class<?> getBeanClass() {
        return this.beanClass;
    }

    @Override
    public Object getBean() {
        return isProxy ? this.proxyBean : this.bean;
    }

    @Override
    public ClassLoader getBeanClassLoader() {
        return this.beanClassLoader;
    }

}
