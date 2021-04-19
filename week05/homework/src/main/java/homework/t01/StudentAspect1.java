package homework.t01;

import io.github.coolhusky.minispring.annotations.*;

import io.github.coolhusky.minispring.aop.JoinPoint;

/**
 * @author jcwang
 */
@Component
@Aspect(order = 2)
public class StudentAspect1 {

    @Pointcut(targetClass = IStudent.class, methodName = "study")
    public void pointcut(){}

    @Before(pointcut = "pointcut()")
    public void before() {
        System.out.println("before study...");
    }

    @After(pointcut = "pointcut()")
    public void after() {
        System.out.println("after study...");
    }

    @Around(pointcut = "pointcut()")
    public Object around(JoinPoint jp) throws Throwable {
        System.out.println("around before study...");
        Object ret = jp.proceed();
        System.out.println("around after study...");
        return ret;
    }
}
