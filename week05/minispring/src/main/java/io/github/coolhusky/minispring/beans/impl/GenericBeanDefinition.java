package io.github.coolhusky.minispring.beans.impl;

import lombok.AllArgsConstructor;
import io.github.coolhusky.minispring.beans.BeanDefinition;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author jcwang
 */
@AllArgsConstructor
public class GenericBeanDefinition implements BeanDefinition {
    private String beanName;
    private Class<?> type;
    private ClassLoader beanClassLoader;

    public GenericBeanDefinition(String beanName, Class<?> type, ClassLoader beanClassLoader) {
        this.beanName = beanName;
        this.type = type;
        this.beanClassLoader = beanClassLoader;
    }

    private Map<String, Object> attributes = new ConcurrentHashMap<>(16);
    @Override
    public String getName() {
        return this.beanName;
    }

    @Override
    public Class<?> getType() {
        return this.type;
    }

    @Override
    public ClassLoader getBeanClassLoader() {
        return this.beanClassLoader;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GenericBeanDefinition that = (GenericBeanDefinition) o;
        return Objects.equals(beanName, that.beanName) && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(beanName, type);
    }

    @Override
    public void setAttribute(String key, Object value) {
        this.attributes.put(key, value);
    }

    @Override
    public Object getAttribute(String key) {
        return this.attributes.get(key);
    }
}
