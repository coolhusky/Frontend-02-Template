package example;

import example.entity.User;
import example.service.PeopleService;
import io.github.coolhusky.minispring.annotations.ComponentScan;
import io.github.coolhusky.minispring.beans.BeanFactory;
import io.github.coolhusky.minispring.beans.impl.AnnotationBeanFactory;
import example.service.UserService;
import io.github.coolhusky.minispring.exception.MiniSpringException;

/**
 * @author jcwang
 */
@ComponentScan(basePackages = {"example"})
public class ExampleApplication {

    public static void main(String[] args) throws MiniSpringException {
        BeanFactory beanFactory = new AnnotationBeanFactory();
        UserService userService = beanFactory.getBean(UserService.class);
        User user = userService.getUser(1L);
        PeopleService peopleService = beanFactory.getBean(PeopleService.class);
        peopleService.eat();
        peopleService.eat();
    }
}
