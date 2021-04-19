package example.aspect;

import example.service.UserService;
import io.github.coolhusky.minispring.annotations.*;
import io.github.coolhusky.minispring.aop.JoinPoint;

/**
 * @author jcwang
 */
@Component
@Aspect(order = 2)
public class UserAspect2 {
    @Pointcut(targetClass = UserService.class, methodName = "getUser", args = {Long.class})
    public void pointcut() {
    }

    @Before(pointcut = "pointcut()")
    public void beforeAdvice() {
        System.out.println("aop2 before getUser is invoked...");
    }

    @After(pointcut = "pointcut()")
    public void afterAdvice() {
        System.out.println("aop2 after getUser is invoked...");
    }

    @Around(pointcut = "pointcut()")
    public void aroundAdvice(JoinPoint joinPoint) throws Throwable {
        System.out.println("aop2 around before....");
        joinPoint.proceed();
        System.out.println("aop2 around after...");
    }
}
