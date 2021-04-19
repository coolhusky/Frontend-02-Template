package minispring.beans.impl;

import com.google.common.collect.ImmutableSet;
import minispring.annotations.Component;
import minispring.beans.BeanDefinition;
import minispring.beans.BeanDefinitionReader;
import minispring.beans.BeanFactoryAware;
import minispring.beans.BeanPostProcessor;
import minispring.utils.ClassUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author jcwang
 */
public class InternalBeanDefinitionReader implements BeanDefinitionReader {
    private static final Set<Class<?>> INTERNAL_BEAN_CLASSES = ImmutableSet.of(
            BeanPostProcessor.class,
            BeanFactoryAware.class
    );

    private static final Predicate<? super Class<?>> CLASS_FILTER = source ->
            INTERNAL_BEAN_CLASSES.stream().anyMatch(target -> ClassUtils.isAssignedFrom(source, target));

    @Override
    public List<BeanDefinition> loadBeanDefinitions(String... locations) {
        if (locations == null || locations.length == 0) {
            return Collections.emptyList();
        }

        return Arrays.stream(locations).map(ClassUtils::loadClasses)
                .flatMap(Set::stream)
                .filter(cls -> !cls.isInterface())
                .filter(CLASS_FILTER)
                .map(cls ->
                        new GenericBeanDefinition(cls.getName(), cls, ClassUtils.getClassLoader())
                ).collect(Collectors.toList());
    }


}
