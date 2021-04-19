package minispring.aop.impl;

import minispring.aop.*;
import minispring.aop.impl.DefaultAdvisorChainFactory;
import minispring.aop.impl.DefaultAdvisorRegistry;
import minispring.support.Ordered;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author jcwang
 */
public class AdvisedSupport implements Advised {
    public static List<MethodInterceptor> NO_PROXY = Collections.emptyList();
    private final AdvisorChainFactory advisorChainFactory = new DefaultAdvisorChainFactory();
    private final Map<MethodCacheKey, List<MethodInterceptor>> methodCache = new ConcurrentHashMap<>(16);
    private final Set<Method> methodIntercepted = new HashSet<>(16);
    private final Class<?> targetClass;
    private final Object target;
    private final ClassLoader classLoader;
    private final List<Advisor> advisors = new ArrayList<>(16);
    private final Object lock = new Object();

    public AdvisedSupport(Object target, ClassLoader classLoader) {
        this.target = target;
        this.targetClass = target.getClass();
        this.classLoader = classLoader;

        this.advisors.addAll(sortedAdvisors());
    }

    private List<Advisor> sortedAdvisors() {
        AdvisorRegistry advisorRegistry = DefaultAdvisorRegistry.getInstance();
        Map<AspectInformation, List<Advisor>> groupByAspect = advisorRegistry.getAdvisors(targetClass).stream()
                .collect(Collectors.groupingBy(Advisor::getAspectInfo));
        List<Advisor> sortedList = groupByAspect.entrySet()
                .stream()
                .sorted(Comparator.comparingInt(entry -> entry.getKey().getOrder()))
                .flatMap(entry -> entry.getValue().stream().sorted(Comparator.comparingInt(Advisor::getOrder)))
                .collect(Collectors.toList());
        return sortedList;
    }

    public List<MethodInterceptor> getChainedInterceptors(Method method, Class<?> targetClass) {
        MethodCacheKey cacheKey = new MethodCacheKey(method);
        List<MethodInterceptor> cached = this.methodCache.get(cacheKey);
        if (cached == null) {
            cached = this.advisorChainFactory.getChainedInterceptors(this, method, targetClass);
            if (cached.isEmpty()) {
                cached = NO_PROXY;
                this.methodCache.put(cacheKey, NO_PROXY);
            } else {
                this.methodCache.put(cacheKey, cached);
            }
        }
        return cached;
    }

    @Override
    public Object getTarget() {
        return this.target;
    }

    @Override
    public ClassLoader getClassLoader() {
        return this.classLoader;
    }

    @Override
    public Class<?> getTargetClass() {
        return this.targetClass;
    }

    @Override
    public Advisor[] getAdvisors() {
        return this.advisors.toArray(new Advisor[0]);
    }

    private static final class MethodCacheKey implements Comparable<MethodCacheKey> {

        private final Method method;

        private final int hashCode;

        public MethodCacheKey(Method method) {
            this.method = method;
            this.hashCode = method.hashCode();
        }

        @Override
        public boolean equals(Object other) {
            return (this == other || (other instanceof MethodCacheKey &&
                    this.method == ((MethodCacheKey) other).method));
        }

        @Override
        public int hashCode() {
            return this.hashCode;
        }

        @Override
        public String toString() {
            return this.method.toString();
        }

        @Override
        public int compareTo(MethodCacheKey other) {
            int result = this.method.getName().compareTo(other.method.getName());
            if (result == 0) {
                result = this.method.toString().compareTo(other.method.toString());
            }
            return result;
        }
    }
}
