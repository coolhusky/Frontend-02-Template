package minispring.aop.impl.advice;

import minispring.aop.Advice;
import minispring.aop.Advisor;
import minispring.aop.AspectInformation;
import minispring.aop.Pointcut;
import minispring.support.Ordered;

import java.util.Objects;

/**
 * @author jcwang
 */
public class AnnotatedAdvisor implements Advisor {
    private final Pointcut pointcut;
    private final Advice advice;
    private Integer order;
    private final AspectInformation aspectInfo;

    public AnnotatedAdvisor( Advice advice, AspectInformation aspectInfo) {
        this.advice = advice;
        this.pointcut = advice.getPointcut();
        this.aspectInfo = aspectInfo;
    }


    @Override
    public AspectInformation getAspectInfo() {
        return this.aspectInfo;
    }

    @Override
    public Pointcut getPointcut() {
        return this.pointcut;
    }

    @Override
    public Advice getAdvice() {
        return this.advice;
    }

    @Override
    public int getOrder() {
        if (this.order == null) {
            this.order = calculateOrder();
        }
        return this.order;
    }

    private int calculateOrder() {
        if (this.advice instanceof MethodAfterAdvice) {
            return Ordered.HIGHEST_PRIORITY;
        }

        if (this.advice instanceof MethodAroundAdvice) {
            return Ordered.HIGHEST_PRIORITY + 1;
        }

        if (this.advice instanceof MethodBeforeAdvice) {
            return Ordered.LOWEST_PRIORITY;
        }

        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnnotatedAdvisor that = (AnnotatedAdvisor) o;
        return Objects.equals(pointcut, that.pointcut) && Objects.equals(advice, that.advice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pointcut, advice);
    }
}
