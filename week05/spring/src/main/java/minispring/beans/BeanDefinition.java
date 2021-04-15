package minispring.beans;

/**
 * @author jcwang
 */
public interface BeanDefinition {

    String getName();

    Class<?> getType();

    ClassLoader getBeanClassLoader();
}
