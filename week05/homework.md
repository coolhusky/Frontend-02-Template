#作业说明

## 模块说明

作业代码都在week05中，涉及3个maven模块

* homework 作业代码
* minispring 简单实现spring ioc, aop(jdk动态代理)
* Spring-demo-starter 实现springboot starter, 通过配置文件动态装配school/klass/student

## 作业说明

### t01 实现简单aop

原理就是反射+JDK动态代理+责任链模式，参考了spring aop的实现。

自定义了几个注解：

* @Aspect 切面类
* @Pointcut 切入点
* @Before 标记before通知
* @After 标记after通知
* @Around 标记环绕通知

几个用来封装切面信息的类：

* Aspect 切面类
* Advice 通知
* Advisor Advice的运行时类通知方法+Aspect实例
* Advised 运行时，连接点+所有的Advice
* Joinpoint 连接点，包含被代理方法、对象信息。
* MethodInterceptor 包装拦截方法
* ReflectiveMethodInvocation MethodInterceptor的执行链，责任链模式
* ...

Aop分为以下几个步骤

#### 1. 读取BeanDefinition

自定义 @Component 注解扫描classpath下的bean，并包装成BeanDefinition

```java
package io.github.coolhusky.minispring.beans;

/**
 * @author jcwang
 */
public interface  BeanDefinition extends AttributeAccessor{

    String getName();

    Class<?> getType();

    ClassLoader getBeanClassLoader();
}
```

通过BeanDefinition创建bean。参考spring，bean的创建也分为instantiation, population, initialization三个阶段。并暴露一个BeanPostProcessor接口。

```java
package io.github.coolhusky.minispring.beans;

import io.github.coolhusky.minispring.beans.impl.BeanAlteredImpl;
import io.github.coolhusky.minispring.exception.MiniSpringException;

/**
 * @author jcwang
 */
public interface BeanPostProcessor {

    default void beforeInstantiation(Class<?> beanClass, String beanName) throws MiniSpringException {

    }

    default BeanAltered postProcessBeforeInitialization(Object bean, String beanName) throws MiniSpringException {
        return new BeanAlteredImpl(bean);
    }

    default BeanAltered postProcessAfterInitialization(Object bean, String beanName) throws MiniSpringException {
        return new BeanAlteredImpl(bean);
    }
}

```

#### 2. 注册Aop

BeanPostProcessor.beforeInstantiation() 在bean实例化之前执行，此过程会将 @Aspect标记的切面类读取，封装到Aspect中, 调用AspectRegistry.registerAspectInfo()注册到AspectRegistry, 但此时@Aspect bean还没有实例化，实例化的时候会调用AspectRegistry.registerAspect注册@Aspect bean实例。

```java
package io.github.coolhusky.minispring.aop;

/**
 * @author jcwang
 */
public interface Aspect extends AspectInformation {

    AspectInstanceFactory getAspectInstanceFactory();

    void setAspectInstanceFactory(AspectInstanceFactory factory);

    void setAspectName(String name);

    default boolean isResolved() {
        return getAspectInstanceFactory() != null;
    }
}

```

```java
/**
 * @author jcwang
 */
public interface AspectRegistry {
		
    void registerAspectInfo(Class<?> aspectClass);

    boolean isAspect(Object candidateAspect);

    boolean registerAspect(Object candidateAspect, String candidateAspectName);
}
```



#### 3. 织入

BeanPostProcessor.postProcessAfterInitialization() 在bean初始化之后调用，此时因为容器已经有bean了，方法中判断如果这个bean被AspectRegistry中的某个Aspect切了，就通过JDK动态代理生成proxy, 这样从容器中就能拿到代理对象了，@Aspect类中的通知也能生效。

```java
package io.github.coolhusky.minispring.aop;

/**
 * @author jcwang
 */
public interface AopProxy {

    Object getProxy();
}

```

```java
package io.github.coolhusky.minispring.aop.impl;

import io.github.coolhusky.minispring.aop.AopProxy;
import io.github.coolhusky.minispring.aop.MethodInterceptor;
import io.github.coolhusky.minispring.aop.impl.advice.ReflectiveMethodInvocation;
import io.github.coolhusky.minispring.exception.MiniSpringRuntimeException;
import io.github.coolhusky.minispring.utils.ArrayUtils;
import io.github.coolhusky.minispring.utils.ReflectionUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 * @author jcwang
 */
public class JdkDynamicAopProxy implements AopProxy, InvocationHandler {
    private final Object target;
    private final Class<?> targetClass;
    private final ClassLoader classLoader;
    private final Class<?>[] interfaces;
    private final AdvisedSupport advised;

    public JdkDynamicAopProxy(AdvisedSupport advised) {
        this.advised = advised;
        this.target = advised.getTarget();
        this.classLoader = advised.getClassLoader();
        if (ArrayUtils.isNullOrEmpty(target.getClass().getInterfaces())) {
            throw new MiniSpringRuntimeException("not support");
        }
        this.targetClass = target.getClass();
        this.interfaces = targetClass.getInterfaces();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 需要拦截的方法调用interceptor chain
        List<MethodInterceptor> chainedInterceptors = this.advised.getChainedInterceptors(method, this.targetClass);
        if (chainedInterceptors == AdvisedSupport.NO_PROXY) {
            // 否则调用原方法
            return ReflectionUtils.invoke(target, method, args);
        }
        return new ReflectiveMethodInvocation(proxy, target, method, args, chainedInterceptors).proceed();
    }

    @Override
    public Object getProxy() {
        return Proxy.newProxyInstance(this.classLoader, this.interfaces, this);
    }
}

```



### t02 bean的装配

* @Component
* XML
* @Bean
* 实现 ImportBeanDefinitionRegistrar接口，扫描自定义@MyBean标记的类加入到容器中

### t08 实现starter

#### 1. 入口

* spring.factories

```properties
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
io.github.coolhusky.spring.starter.ExampleSpringBootAutoConfiguration
```

* 入口类

```java
package io.github.coolhusky.spring.starter;

import io.github.coolhusky.spring.starter.beans.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import io.github.coolhusky.spring.starter.props.ExampleStudentAutoConfigProperties;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jcwang
 */
@Configuration
@ConditionalOnClass({School.class, ISchool.class, Klass.class, Student.class})
@EnableConfigurationProperties(ExampleStudentAutoConfigProperties.class)
public class ExampleSpringBootAutoConfiguration {

    @Autowired
    private ExampleStudentAutoConfigProperties properties;

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "example.autoconfig.school", value = "enabled", havingValue = "true")
    ISchool school() {
        return new School();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "example.autoconfig.klass", value = "enabled", havingValue = "true")
    Klass klass() {
        return new Klass();
    }

    @Bean
    @ConditionalOnMissingBean
    List<Student> student() {
        return properties.getStudents().stream()
                .map(s -> new Student(s.getId(), s.getName()))
                .collect(Collectors.toList());
    }
}

```

#### 2. 开关

如果配置了example.autoconfig.klass.enabled=true, 就会创建Klass，School类的创建类似。Student支持创建多个。

```yaml
example:
  autoconfig:
    students:
      - id: 1
        name: student001
      - id: 2
        name: student002
    klass:
      enabled: true

    school:
      enabled: true
```







