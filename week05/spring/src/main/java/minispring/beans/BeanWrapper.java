package minispring.beans;

/**
 * @author jcwang
 */
public interface BeanWrapper {

    boolean isProxy();


    void setProxy(Object proxy);

    String getBeanName();

    Class<?> getBeanClass();

    Object getBean();

    ClassLoader getBeanClassLoader();
}
