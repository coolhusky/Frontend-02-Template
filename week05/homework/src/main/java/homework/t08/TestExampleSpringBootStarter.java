package homework.t08;

import io.github.coolhusky.spring.starter.beans.ISchool;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author jcwang
 */
@SpringBootApplication
public class TestExampleSpringBootStarter {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(TestExampleSpringBootStarter.class);
        ISchool school = ctx.getBean(ISchool.class);
        school.ding();
        System.out.println("ok");
    }
}
