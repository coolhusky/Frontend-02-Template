package homework.t02;

import homework.t02.beanconfig.MyBeanBeanDefinitionRegistrar;
import homework.t02.dao.UserDaoFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author jcwang
 */
@Configuration
@Import({MyBeanBeanDefinitionRegistrar.class})
public class MyBeanAutoConfig {
    @Bean
    public UserDaoFactoryBean userDaoFactoryBean() {
        return new UserDaoFactoryBean();
    }
}
