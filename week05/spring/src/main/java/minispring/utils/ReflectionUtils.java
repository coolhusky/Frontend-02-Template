package minispring.utils;

import minispring.exception.MiniSpringRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

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
}
