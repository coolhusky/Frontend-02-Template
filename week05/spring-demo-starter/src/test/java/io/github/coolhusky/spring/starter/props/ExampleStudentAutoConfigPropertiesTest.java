package io.github.coolhusky.spring.starter.props;


import io.github.coolhusky.spring.starter.ExampleSpringBootAutoConfiguration;
import io.github.coolhusky.spring.starter.beans.ISchool;
import io.github.coolhusky.spring.starter.beans.Klass;
import io.github.coolhusky.spring.starter.beans.Student;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author jcwang
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ExampleSpringBootAutoConfiguration.class)
public class ExampleStudentAutoConfigPropertiesTest {
    @Autowired
    private ExampleStudentAutoConfigProperties properties;
    @Autowired
    private List<Student> students;
    @Autowired
    private ISchool school;
    @Autowired
    private Klass klass;

    @Test
    public void testStarter() {
        students.forEach(System.out::println);
        school.ding();
        klass.dong();
    }

}