package minispring.beans.impl;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import minispring.annotations.Autowired;
import minispring.annotations.ComponentScan;
import minispring.beans.*;
import minispring.exception.MiniSpringException;
import minispring.exception.MiniSpringRuntimeException;
import minispring.utils.ClassUtils;
import minispring.utils.ReflectionUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author jcwang
 */
public class AnnotationBeanFactory implements BeanFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(AnnotationBeanFactory.class);

    private static final String ROOT_MINI_SPRING = "minispring";
    private static final Set<Class<?>> HIGH_PRIORITY_BEAN_CLASSES = ImmutableSet.of(
            BeanPostProcessor.class,
            BeanFactoryAware.class
    );

    private final Map<Class<?>, BeanDefinition> BD_MAP = new ConcurrentHashMap<>(16);
    private final Map<Class<?>, List<BeanWrapper>> BEAN_TYPE_MAP = new ConcurrentHashMap<>(16);
    private final Map<String, BeanWrapper> BEAN_NAME_MAP = new ConcurrentHashMap<>(16);
    private final List<BeanDefinitionReader> beanDefinitionReaders;
    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>(16);
    private final List<BeanFactoryAware> beanFactoryAwares = new ArrayList<>(16);
    private final ClassLoader classLoader = ClassUtils.getClassLoader();
    private final String[] basePackages;

    public AnnotationBeanFactory() {
        this.beanDefinitionReaders = ImmutableList.of(
                new AnnotationBeanDefinitionReader(),
                new InternalBeanDefinitionReader());
        this.basePackages = searchBasePackages();
        refresh();
//        System.out.println("bean factory init complete...");
    }


    private void refresh() {
        // load bean definitions
        loadBeanDefinitions();
        // create beans
        createBeans();
    }

    private void loadBeanDefinitions() {
        this.beanDefinitionReaders.stream().map(bdr -> {
            if (bdr instanceof InternalBeanDefinitionReader) {
                return bdr.loadBeanDefinitions(ROOT_MINI_SPRING);
            }
            return bdr.loadBeanDefinitions(basePackages);
        }).flatMap(Collection::stream).forEach(bd -> BD_MAP.put(bd.getType(), bd));
    }


    private void createBeans() {
        // 实例化优先级高的bean
        highPriorityInstantiation();
        // invoke bean awares
        invokeBeanFactoryAwares();
        // instantiation
        BD_MAP.values().forEach(this::instantiateBean);
        // populate
        BEAN_NAME_MAP.values().forEach(this::populateBean);
        // initialization
        BEAN_NAME_MAP.values().forEach(this::initializeBean);
    }

    private void invokeBeanFactoryAwares() {
        this.beanFactoryAwares.forEach(bfa -> bfa.setBeanFactory(this));
    }

    private void highPriorityInstantiation() {
        BD_MAP.values()
                .stream()
                .filter(bd ->
                        HIGH_PRIORITY_BEAN_CLASSES.stream().anyMatch(target ->
                                ClassUtils.isAssignedFrom(bd.getType(), target)))
                .forEach(this::doCreateBean);
        this.beanPostProcessors.addAll(getBeans(BeanPostProcessor.class));
        this.beanFactoryAwares.addAll(getBeans(BeanFactoryAware.class));
    }

    private void populateBean(BeanWrapper bw) {
        Class<?> beanClass = bw.getBeanClass();
        Object bean = bw.getBean();

        Arrays.stream(beanClass.getDeclaredFields())
                .filter(f -> !f.getType().isPrimitive() && f.isAnnotationPresent(Autowired.class))
                .forEach(f -> {
                    Class<?> fType = f.getType();
                    if (ClassUtils.isCollection(fType)) {
                        Class<?> genericType = ClassUtils.getCollGenericType(fType, f.getGenericType());
                        List<BeanWrapper> beanWrappers = BEAN_TYPE_MAP.get(genericType);
                        if (CollectionUtils.isNotEmpty(beanWrappers)) {
                            ReflectionUtils.setField(bean, f,
                                    beanWrappers.stream().map(BeanWrapper::getBean).collect(Collectors.toList()));
                        }
                    } else {
                        try {
                            Object fBean = getBean(fType);
                            if (bean != null) {
                                ReflectionUtils.setField(bean, f, fBean);
                            }
                        } catch (MiniSpringException e) {
                            LOGGER.error("populate beans failed", e);
                        }
                    }
                });
    }

    private BeanWrapper instantiateBean(BeanDefinition bd) {
        this.beanPostProcessors.forEach(bpp -> {
            try {
                bpp.beforeInstantiation(bd.getType(), bd.getName());
            } catch (MiniSpringException e) {
                LOGGER.error("before instantiation", e);
                throw new MiniSpringRuntimeException(e);
            }
        });
        return doCreateBean(bd);
    }

    private BeanWrapper doCreateBean(BeanDefinition bd) {
        String bName = bd.getName();
        Object bean = ReflectionUtils.newInstance(bd.getType());
        GenericBeanWrapper beanWrapper = new GenericBeanWrapper(bName, bean, bd.getType(), bd.getBeanClassLoader());
        if (BEAN_NAME_MAP.containsKey(bd.getName())) {
            BeanWrapper existedBw = BEAN_NAME_MAP.get(bd.getName());
            if (ClassUtils.equals(bd.getType(), existedBw.getBeanClass())) {
                return existedBw;
            } else {
                throw new MiniSpringRuntimeException("bean name duplicate: " + bName);
            }
        }
        BEAN_NAME_MAP.put(bName, beanWrapper);
        mergeBeans(bd.getType(), beanWrapper);
        return beanWrapper;
    }

    private void mergeBeans(Class<?> beanClass, BeanWrapper beanWrapper) {
        BEAN_TYPE_MAP.computeIfAbsent(beanClass, k -> new ArrayList<>(4));
        BEAN_TYPE_MAP.get(beanClass).add(beanWrapper);
        // superclass
        Class<?> superclass = beanClass.getSuperclass();
        if (superclass == null || ClassUtils.equals(Object.class, superclass)) {
            // interfaces
            Class<?>[] interfaces = beanClass.getInterfaces();
            if (interfaces == null || interfaces.length == 0) {
                return;
            } else {
                for (Class<?> anInterface : interfaces) {
                    mergeBeans(anInterface, beanWrapper);
                }
            }

        } else {
            mergeBeans(superclass, beanWrapper);
        }
    }

    private String[] searchBasePackages() {
        Set<Class<?>> classes = ClassUtils.loadClasses(StringUtils.EMPTY);
        return classes.stream()
                .filter(cls -> cls.isAnnotationPresent(ComponentScan.class))
                .map(cls -> cls.getAnnotation(ComponentScan.class))
                .flatMap(anno -> Arrays.stream(anno.basePackages()))
                .collect(Collectors.toList())
                .toArray(new String[]{});
    }

    private void initializeBean(BeanWrapper bw) {
        // before
        applyPostProcessorsBeforeInitialization(bw);
        // do init
        invokeInitMethod(bw);
        // after
        applyPostProcessorsAfterInitialization(bw);
    }

    private void applyPostProcessorsBeforeInitialization(BeanWrapper bw) {
        this.beanPostProcessors.forEach(bpp -> {
            try {
                BeanAltered beanAltered  = bpp.postProcessBeforeInitialization(bw.getBean(), bw.getBeanName());
                if (beanAltered.altered()) {
                    bw.setProxy(beanAltered.after());
                }
            } catch (MiniSpringException e) {
                LOGGER.error("PostProcessorsBeforeInitialization", e);
                throw new MiniSpringRuntimeException(e);
            }
        });
    }

    private void invokeInitMethod(BeanWrapper bw) {
        // TODO
    }

    private void applyPostProcessorsAfterInitialization(BeanWrapper bw) {
        this.beanPostProcessors.forEach(bpp -> {
            try {
                BeanAltered beanAltered = bpp.postProcessAfterInitialization(bw.getBean(), bw.getBeanName());
                if (beanAltered.altered()) {
                    bw.setProxy(beanAltered.after());
                }

            } catch (MiniSpringException e) {
                LOGGER.error("PostProcessorsBeforeInitialization", e);
                throw new MiniSpringRuntimeException(e);
            }
        });
    }


    @SuppressWarnings("unchecked")
    @Override
    public <T> T getBean(Class<T> requiredType) throws MiniSpringException {
        List<BeanWrapper> matchedBeans = BEAN_TYPE_MAP.get(requiredType);

        if (CollectionUtils.isEmpty(matchedBeans)) {
            throw new MiniSpringException("bean of type " + requiredType.getName() + " not found");
        }
        if (matchedBeans.size() > 1) {
            throw new MiniSpringException("more than one bean of type " + requiredType.getName() + "found");
        }
        return (T) matchedBeans.get(0).getBean();
    }

    @Override
    public <T> T getBean(String beanName, Class<?> requiredType) throws MiniSpringException {
        BeanWrapper beanWrapper = BEAN_NAME_MAP.get(beanName);
        if (beanWrapper == null) {
            throw new MiniSpringException("no such bean of name " + beanName);
        }
        if (!ClassUtils.isAssignedFrom(beanWrapper.getBeanClass(), requiredType)) {
            throw new MiniSpringException("no such bean of name " + beanName + " for type " + requiredType.getName());
        }
        return (T) beanWrapper.getBean();

    }

    @Override
    public ClassLoader getClassLoader() {
        return this.classLoader;
    }

    @Override
    public BeanDefinition getBeanDefinition(Class<?> beanClass) {
        return BD_MAP.get(beanClass);
    }

    private <T> List<T> getBeans(Class<?> requiredType) {
        List<BeanWrapper> beanWrappers = BEAN_TYPE_MAP.get(requiredType);
        if (CollectionUtils.isEmpty(beanWrappers)) {
            return Collections.emptyList();
        }
        return beanWrappers.stream().map(bw -> (T) bw.getBean()).collect(Collectors.toList());
    }
}
