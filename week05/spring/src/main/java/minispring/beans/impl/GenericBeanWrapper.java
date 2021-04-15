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
@AllArgsConstructor
public class GenericBeanWrapper implements BeanWrapper {
    private String beanName;
    private Object bean;
    private Class<?> beanClass;
    private ClassLoader beanClassLoader;


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
        return this.bean;
    }

    @Override
    public ClassLoader getBeanClassLoader() {
        return this.beanClassLoader;
    }
}
