package minispring.utils;

import minispring.exception.MiniSpringRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.*;
import java.util.Collection;

/**
 * @author jinchaowang
 */
public class ReflectionUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReflectionUtils.class);

    private ReflectionUtils(){}

    public static Object newInstance(Class<?> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            LOGGER.error("newInstance", e);
            throw new MiniSpringRuntimeException(e);
        }
    }

    public static void setField(Object target, Field field, Object fieldValue) {
        field.setAccessible(true);
        try {
            field.set(target, fieldValue);
        } catch (IllegalAccessException e) {
            LOGGER.error("set field failed", e);
            throw new MiniSpringRuntimeException(e);
        }
    }

    public static boolean isInstanceOf(Object source, Class<?> clazz) {
        return ClassUtils.isAssignedFrom(source.getClass(), clazz);
    }
    public static void makeAccessible(AccessibleObject accessibleObject) {
        accessibleObject.setAccessible(true);
    }

    public static Object invoke(Object target, Method method, Object[] args) throws Throwable {
        makeAccessible(method);
        try {
            return method.invoke(target, args);
        } catch (Throwable e) {
            LOGGER.error("invoke method failed", e);
            throw e;
        }
    }

    public static Method getMethod(Class<?> targetClass, String methodName, Class<?>[] args) {
        try {
            return targetClass.getDeclaredMethod(methodName, args);
        } catch (NoSuchMethodException e) {
            LOGGER.error("get method", e);
            throw new MiniSpringRuntimeException(e);
        }
    }
}
