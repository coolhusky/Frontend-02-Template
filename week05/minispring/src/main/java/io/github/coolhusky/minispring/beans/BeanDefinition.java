package io.github.coolhusky.minispring.beans;

/**
 * @author jcwang
 */
public interface  BeanDefinition extends AttributeAccessor{

    String getName();

    Class<?> getType();

    ClassLoader getBeanClassLoader();
}
