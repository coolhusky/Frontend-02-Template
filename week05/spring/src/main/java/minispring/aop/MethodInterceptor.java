package minispring.aop;

public interface MethodInterceptor {

    Object invoke(MethodInvocation mi) throws Throwable;
}
