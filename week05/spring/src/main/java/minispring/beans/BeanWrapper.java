package minispring.beans;

/**
 * @author jcwang
 */
public interface BeanWrapper {


    String getBeanName();

    Class<?> getBeanClass();

    Object getBean();

    ClassLoader getBeanClassLoader();
}
