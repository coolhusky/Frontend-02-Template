package example.aspect;

import example.service.PeopleService;
import example.service.UserService;
import minispring.annotations.*;
import minispring.aop.JoinPoint;

/**
 * @author jcwang
 */
//@Component
@Aspect(order = 4)
public class UserAspect4 {
    @Pointcut(targetClass = PeopleService.class, methodName = "eat")
    public void pointcut() {
    }

    @Before(pointcut = "pointcut()")
    public void beforeAdvice() {
//        System.out.println("aop4 before getUser is invoked...");
    }

    @After(pointcut = "pointcut()")
    public void afterAdvice() {
//        System.out.println("aop4 after getUser is invoked...");
    }

    @Around(pointcut = "pointcut()")
    public void aroundAdvice(JoinPoint joinPoint) throws Throwable {
//        System.out.println("aop4 around before....");
        joinPoint.proceed();
//        System.out.println("aop4 around after...");
    }
}
