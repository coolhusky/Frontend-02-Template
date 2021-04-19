package homework.t01;

import minispring.annotations.*;

import java.util.Date;

/**
 * @author jcwang
 */
@Component
@Aspect(order = 1)
public class StudentAspect2 {
    @Pointcut(targetClass = IStudent.class, methodName = "study")
    public void pointcut(){}


    @Before(pointcut = "pointcut()")
    public void before() {
        System.out.println("study start at: " + new Date(System.currentTimeMillis()));
    }

    @After(pointcut = "pointcut()")
    public void after() {
        System.out.println("study stopped at: " + new Date(System.currentTimeMillis()));
    }
}
