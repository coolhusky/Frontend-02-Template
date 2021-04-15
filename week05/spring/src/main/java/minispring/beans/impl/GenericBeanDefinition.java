package minispring.beans.impl;

import lombok.AllArgsConstructor;
import minispring.beans.BeanDefinition;

/**
 * @author jcwang
 */
@AllArgsConstructor
public class GenericBeanDefinition implements BeanDefinition {
    private String beanName;
    private Class<?> type;
    private ClassLoader beanClassLoader;

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


}
