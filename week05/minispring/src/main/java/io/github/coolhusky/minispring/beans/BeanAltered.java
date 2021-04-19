package io.github.coolhusky.minispring.beans;

/**
 * @author jcwang
 */
public interface BeanAltered {


    Object before();
    Object after();
    boolean altered();
}
