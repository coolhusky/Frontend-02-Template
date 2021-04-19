package minispring.aop;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author jcwang
 */
public interface AdvisorChainFactory {

    List<MethodInterceptor>  getChainedInterceptors(Advised config, Method method, Class<?> targetClass);
}
