package homework.t02;

import homework.t02.dao.OrderDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author jcwang
 */
@Configuration
public class BeanConfig {
    @Bean
    public OrderDao orderDao() {
        return new OrderDao();
    }
}
