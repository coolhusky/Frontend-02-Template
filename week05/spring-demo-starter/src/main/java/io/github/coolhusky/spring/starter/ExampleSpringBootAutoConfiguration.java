package io.github.coolhusky.spring.starter;

import io.github.coolhusky.spring.starter.beans.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import io.github.coolhusky.spring.starter.props.ExampleStudentAutoConfigProperties;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jcwang
 */
@Configuration
@ConditionalOnClass({School.class, ISchool.class, Klass.class, Student.class})
@EnableConfigurationProperties(ExampleStudentAutoConfigProperties.class)
public class ExampleSpringBootAutoConfiguration {

    @Autowired
    private ExampleStudentAutoConfigProperties properties;

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "example.autoconfig.school", value = "enabled", havingValue = "true")
    ISchool school() {
        return new School();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "example.autoconfig.klass", value = "enabled", havingValue = "true")
    Klass klass() {
        return new Klass();
    }

    @Bean
    @ConditionalOnMissingBean
    List<Student> student() {
        return properties.getStudents().stream()
                .map(s -> new Student(s.getId(), s.getName()))
                .collect(Collectors.toList());
    }
}
