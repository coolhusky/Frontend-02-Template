package minispring.beans.impl;

import minispring.annotations.Autowired;
import minispring.annotations.ComponentScan;
import minispring.beans.BeanDefinition;
import minispring.beans.BeanDefinitionReader;
import minispring.beans.BeanFactory;
import minispring.beans.BeanWrapper;
import minispring.exception.MiniSpringException;
import minispring.utils.ClassUtils;
import minispring.utils.ReflectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author jcwang
 */
public class AnnotationBeanFactory implements BeanFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(AnnotationBeanFactory.class);

    private static final Map<String, BeanDefinition> BD_MAP = new ConcurrentHashMap<>(16);
    private static final Map<Class<?>, BeanWrapper> BEAN_MAP = new ConcurrentHashMap<>(16);
    private static final String ROOT = StringUtils.EMPTY;

    private final BeanDefinitionReader beanDefinitionReader;


    public AnnotationBeanFactory() {
        this.beanDefinitionReader = new AnnotationBeanDefinitionReader();
        createBeans();
    }


    private void createBeans() {
        String[] basePackages = searchBasePackages();
        loadBeanDefinitions(basePackages);
        doCreateBeans();
    }

    private void loadBeanDefinitions(String[] basePackages) {
        List<BeanDefinition> bds = this.beanDefinitionReader.loadBeanDefinitions(basePackages);
        bds.forEach(bd -> BD_MAP.put(bd.getName(), bd));
    }

    private void doCreateBeans() {
        // instantiation
        BD_MAP.forEach((name, bd) -> {
            Object bean = ReflectionUtils.newInstance(bd.getType());
            GenericBeanWrapper beanWrapper = new GenericBeanWrapper(name, bean, bd.getType(), bd.getBeanClassLoader());
            BEAN_MAP.putIfAbsent(bd.getType(), beanWrapper);
        });
        // populate
        BEAN_MAP.values().forEach(bw -> {
            Class<?> beanClass = bw.getBeanClass();
            Object bean = bw.getBean();
            Arrays.stream(beanClass.getDeclaredFields())
                    .filter(f -> !f.getType().isPrimitive() && f.isAnnotationPresent(Autowired.class) )
                    .forEach(f -> {
                        Class<?> fType = f.getType();
                        try {
                            Object fBean = getBean(fType);
                            if (bean != null) {
                                ReflectionUtils.setField(bean, f, fBean);
                            }
                        } catch (MiniSpringException e) {
                            LOGGER.error("populate beans failed", e);
                        }
                    });

        });
    }

    private String[] searchBasePackages() {
        Set<Class<?>> classes = ClassUtils.loadClasses(ROOT);
        return classes.stream()
                .filter(cls -> cls.isAnnotationPresent(ComponentScan.class))
                .map(cls -> cls.getAnnotation(ComponentScan.class))
                .flatMap(anno -> Arrays.stream(anno.basePackages()))
                .collect(Collectors.toList())
                .toArray(new String[]{});
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getBean(Class<T> requiredType) throws MiniSpringException {
        BeanWrapper beanWrapper = BEAN_MAP.get(requiredType);
        if (beanWrapper != null) {
            return (T)beanWrapper.getBean();
        }
        List<BeanWrapper> matchedBeans = BEAN_MAP.values().stream().filter(bw -> requiredType.isAssignableFrom(bw.getBeanClass()))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(matchedBeans)) {
            throw new MiniSpringException("bean of type " + requiredType.getName() + " not found");
        }
        if (matchedBeans.size() > 1) {
            throw new MiniSpringException("more than one bean of type " + requiredType.getName() + "found");
        }
        return (T) matchedBeans.get(0).getBean();
    }
}
