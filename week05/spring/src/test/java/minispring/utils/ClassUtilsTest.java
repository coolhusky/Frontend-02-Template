package minispring.utils;


import org.junit.Test;

import java.util.Set;

/**
 * @author jcwang
 */
public class ClassUtilsTest {

    @Test
    public void loadClasses() {
        Set<Class<?>> classSet = ClassUtils.loadClasses("/");
        classSet.forEach(System.out::println);
    }
}