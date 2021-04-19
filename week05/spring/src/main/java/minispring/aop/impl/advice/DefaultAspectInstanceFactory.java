package minispring.aop.impl.advice;

import minispring.aop.AspectInstanceFactory;

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
