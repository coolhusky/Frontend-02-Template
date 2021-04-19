package homework.t02;

import example.entity.User;
import homework.t02.entity.Order;
import homework.t02.service.OrderService;
import homework.t02.service.UserService;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author jcwang
 */
public class TestSpringIoc {
    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        OrderService orderService = ctx.getBean(OrderService.class);
        Order order = orderService.getOrder(1);
        System.out.println(order);

        UserService userService = ctx.getBean(UserService.class);
        User user = userService.getUser(1);
        System.out.println(user);
    }
}
