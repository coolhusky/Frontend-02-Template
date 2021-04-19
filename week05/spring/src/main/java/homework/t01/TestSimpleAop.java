package homework.t01;

import minispring.annotations.ComponentScan;
import minispring.beans.BeanFactory;
import minispring.beans.impl.AnnotationBeanFactory;
import minispring.exception.MiniSpringException;

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
