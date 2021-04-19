package io.github.coolhusky.minispring.aop.impl.advice;

import io.github.coolhusky.minispring.aop.AspectInstanceFactory;

/**
 * @author jcwang
 */
public class DefaultAspectInstanceFactory implements AspectInstanceFactory {
    private final Object aspectBean;

    public DefaultAspectInstanceFactory(Object aspectBean) {
        this.aspectBean = aspectBean;
    }


    @Override
    public Object getAspectInstance() {
        return this.aspectBean;
    }
}
