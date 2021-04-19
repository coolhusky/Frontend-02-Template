package minispring.utils;


import example.entity.User;
import example.service.UserService;
import example.service.impl.UserServiceImpl;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

    @Test
    public void testSuperclass() {
        UserService userService = new UserServiceImpl();
        System.out.println(userService.getClass());
        Arrays.stream(userService.getClass().getInterfaces()).forEach(System.out::println);
        System.out.println(UserService.class.getInterfaces().length);
        System.out.println(userService.getClass().getGenericSuperclass());
    }

    @Test
    public void testGenericType() throws Exception{
        TestClass testClsObj = new TestClass();
        Class<?> clazz = TestClass.class;
        Field field = clazz.getDeclaredField("userList");
        System.out.println(ClassUtils.isCollection(field.getType()));
        Class<?> fType = field.getType();
        Type fGType = field.getGenericType();
        System.out.println(fType.getTypeName());
        System.out.println(((ParameterizedType)fGType).getActualTypeArguments()[0]);
        Arrays.stream(clazz.getTypeParameters()).forEach(t -> System.out.println(t.getName()));
    }

    class TestClass {
        List<User> userList = new ArrayList<>();
    }
}