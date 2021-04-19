package example.aspect;

import example.service.UserService;
import io.github.coolhusky.minispring.annotations.*;
import io.github.coolhusky.minispring.aop.JoinPoint;

/**
 * @author jcwang
 */
@Component
@Aspect(order = 1)
public class UserAspect1 {
    @Pointcut(targetClass = UserService.class, methodName = "getUser", args = {Long.class})
    public void pointcut() {
    }

    @Before(pointcut = "pointcut()")
    public void beforeAdvice() {
        System.out.println("aop1 before getUser is invoked...");
    }

    @After(pointcut = "pointcut()")
    public void afterAdvice() {
        System.out.println("aop1 after getUser is invoked...");
    }

    @Around(pointcut = "pointcut()")
    public void aroundAdvice(JoinPoint joinPoint) throws Throwable {
        System.out.println("aop1 around before....");
        joinPoint.proceed();
        System.out.println("aop1 around after...");
    }
}
