package minispring.example;

import minispring.annotations.ComponentScan;
import minispring.beans.BeanFactory;
import minispring.beans.impl.AnnotationBeanFactory;
import minispring.example.entity.User;
import minispring.example.service.UserService;
import minispring.exception.MiniSpringException;

/**
 * @author jcwang
 */
@ComponentScan(basePackages = {"minispring.example"})
public class ExampleApplication {

    public static void main(String[] args) throws MiniSpringException {
       BeanFactory beanFactory = new AnnotationBeanFactory();
        UserService userService = beanFactory.getBean(UserService.class);
        User user = userService.getUser(1L);
        System.out.println(user);
    }
}
