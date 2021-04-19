package homework.t01;

import io.github.coolhusky.minispring.annotations.ComponentScan;
import io.github.coolhusky.minispring.beans.BeanFactory;
import io.github.coolhusky.minispring.beans.impl.AnnotationBeanFactory;
import io.github.coolhusky.minispring.exception.MiniSpringException;

/**
 * @author jcwang
 */
@ComponentScan(basePackages = "homework.t01")
public class TestSimpleAop {
    public static void main(String[] args) throws MiniSpringException {
        BeanFactory bf = new AnnotationBeanFactory();
        IStudent student = bf.getBean(IStudent.class);
        student.study();
    }
}
