package io.github.coolhusky.spring.starter.beans;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.io.Serializable;


@Data
@NoArgsConstructor
@ToString
public class Student implements Serializable, BeanNameAware, BeanPostProcessor {

    private int id;
    private String name;
    private String beanName;

    public Student(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
