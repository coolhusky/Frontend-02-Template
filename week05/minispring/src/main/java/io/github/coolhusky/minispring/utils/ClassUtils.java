package io.github.coolhusky.minispring.utils;

import io.github.coolhusky.minispring.exception.MiniSpringRuntimeException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

/**
 * @author jcwang
 */
public class ClassUtils {
    private static final String ROOT = StringUtils.EMPTY;
    private static final Logger LOGGER = LoggerFactory.getLogger(ClassUtils.class);
    private static final Map<String, Set<Class<?>>> LOAD_CACHE = new ConcurrentHashMap<>(16);

    private ClassUtils() {
    }

    public static boolean isAnnotated(Class<?> clazz, Class<? extends Annotation> annotationClass) {
        return clazz.isAnnotationPresent(annotationClass);
    }


    public static boolean isCollection(Class<?> clazz) {
        return isAssignedFrom(clazz, Collection.class);
    }

    public static Class<?> getCollGenericType(Class<?> collClass, Type genericType) {
        if (!(genericType instanceof ParameterizedType)) {
            throw new MiniSpringRuntimeException("generic type not specified");
        }

        return (Class<?>) ((ParameterizedType) genericType).getActualTypeArguments()[0];
    }

    public static boolean isAssignedFrom(Class<?> sub, Class<?> sup) {
        return sup.isAssignableFrom(sub);
    }

    public static boolean equals(Class<?> clsA, Class<?> clsB) {
        return Objects.equals(clsA, clsB);
    }


    public static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    public static Class<?> loadClass(String className) {
        return loadClass(className, true);
    }

    public static Class<?> loadClass(String className, boolean isInitialized) {
        Class<?> cls;
        try {
            cls = Class.forName(className, isInitialized, getClassLoader());
        } catch (ClassNotFoundException e) {
            LOGGER.error("load class failure", e);
            throw new RuntimeException(e);
        }
        return cls;
    }

    public static Set<Class<?>> loadSubclasses(Class<?> superClass,String... basePackages) {
        Set<Class<?>> allClasses = loadClasses(basePackages);
        return allClasses.stream().filter(cls -> isAssignedFrom(cls, superClass)).collect(Collectors.toSet());
    }

    public static Set<Class<?>> loadClasses(String... packageNames) {
        Set<Class<?>> classSet = new HashSet<>(16);
        for (String packageName : packageNames) {
            classSet.addAll(loadClasses(packageName));
        }
        return classSet;
    }

    public static Set<Class<?>> loadClasses(String packageName) {
        if (LOAD_CACHE.containsKey(packageName)) {
            return LOAD_CACHE.get(packageName);
        }
        Set<Class<?>> classSet = new HashSet<>(16);
        try {
            Enumeration<URL> urls = getClassLoader().getResources(packageName.replace(".", "/"));
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                if (url == null) {
                    continue;
                }

                String protocol = url.getProtocol();
                if ("file".equals(protocol)) {
                    String packagePath = url.getPath().replaceAll("%20", " ");
                    addClass(classSet, packagePath, packageName);
                } else if ("jar".equals(protocol)) {
                    JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
                    if (jarURLConnection != null) {
                        JarFile jarFile = jarURLConnection.getJarFile();
                        if (jarFile != null) {
                            Enumeration<JarEntry> jarEntries = jarFile.entries();
                            while (jarEntries.hasMoreElements()) {
                                JarEntry jarEntry = jarEntries.nextElement();
                                String jarEntryName = jarEntry.getName();
                                if (jarEntryName.endsWith(".class")) {
                                    String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replaceAll("/", ".");
                                    doAddClass(classSet, className);
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("load classes", e);
            throw new MiniSpringRuntimeException(e);
        }
        LOAD_CACHE.put(packageName, classSet);
        return Collections.unmodifiableSet(classSet);
    }

    private static void addClass(Set<Class<?>> classSet, String packagePath, String packageName) {
        File[] files = new File(packagePath).listFiles(file -> (file.isFile() && file.getName().endsWith(".class")) || file.isDirectory());
        if (files == null) {
            throw new MiniSpringRuntimeException("load .class failed");
        }
        for (File file : files) {
            String fileName = file.getName();
            if (file.isFile()) {
                String className = fileName.substring(0, fileName.lastIndexOf("."));
                if (StringUtils.isNotEmpty(packageName)) {
                    className = packageName + "." + className;
                }
                doAddClass(classSet, className);
            } else {
                String subPackagePath = fileName;
                if (StringUtils.isNotEmpty(packagePath)) {
                    subPackagePath = packagePath + "/" + subPackagePath;
                }
                String subPackageName = fileName;
                if (StringUtils.isNotEmpty(packageName)) {
                    subPackageName = packageName + "." + subPackageName;
                }
                addClass(classSet, subPackagePath, subPackageName);
            }
        }
    }

    private static void doAddClass(Set<Class<?>> classSet, String className) {
        Class<?> cls = loadClass(className, false);
        classSet.add(cls);
    }

}
