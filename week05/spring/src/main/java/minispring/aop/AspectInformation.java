package minispring.aop;

import minispring.support.Ordered;

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
