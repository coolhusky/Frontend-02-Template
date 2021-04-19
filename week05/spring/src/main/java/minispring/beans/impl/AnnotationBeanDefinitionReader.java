package minispring.beans.impl;

import minispring.annotations.Component;
import minispring.beans.BeanDefinition;
import minispring.beans.BeanDefinitionReader;
import minispring.utils.ClassUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author jcwang
 */
public class AnnotationBeanDefinitionReader implements BeanDefinitionReader {

    @Override
    public List<BeanDefinition> loadBeanDefinitions(String... locations) {
        if (locations == null || locations.length == 0) {
            return Collections.emptyList();
        }

        return Arrays.stream(locations).map(ClassUtils::loadClasses)
                .flatMap(Set::stream)
                .filter(cls -> !cls.isInterface())
                .filter(cls -> cls.isAnnotationPresent(Component.class))
                .map(cls ->
                    new GenericBeanDefinition(cls.getName(), cls, ClassUtils.getClassLoader())
                ).collect(Collectors.toList());
    }
}
