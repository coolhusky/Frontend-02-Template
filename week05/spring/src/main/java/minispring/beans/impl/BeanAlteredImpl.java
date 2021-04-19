package minispring.beans.impl;

import lombok.AllArgsConstructor;
import minispring.beans.BeanAltered;

/**
 * @author jcwang
 */
@AllArgsConstructor
public class BeanAlteredImpl implements BeanAltered {
    private final Object before;
    private final Object after;
    private final boolean altered;

    public BeanAlteredImpl(Object unchangedBean) {
        this.before = unchangedBean;
        this.after = unchangedBean;
        this.altered = false;
    }

    @Override
    public Object before() {
        return this.before;
    }

    @Override
    public Object after() {
        return this.after;
    }

    @Override
    public boolean altered() {
        return this.altered;
    }
}
