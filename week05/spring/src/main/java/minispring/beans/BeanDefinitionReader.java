package minispring.beans;

import java.util.List;

/**
 * @author jcwang
 */
public interface BeanDefinitionReader {

    List<BeanDefinition> loadBeanDefinitions(String... locations);
}
