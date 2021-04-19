package io.github.coolhusky.minispring.aop;

import io.github.coolhusky.minispring.support.Ordered;

import java.util.List;

/**
 * wrap aspect meta info
 *
 * @author jcwang
 */
public interface AspectInformation extends Ordered {

    String getName();

    Class<?> getAspectClass();

    int getOrder();

    List<Advisor> getAdvisors();
}
