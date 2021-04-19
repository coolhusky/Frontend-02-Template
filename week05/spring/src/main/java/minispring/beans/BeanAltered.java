package minispring.beans;

import minispring.beans.impl.BeanAlteredImpl;

/**
 * @author jcwang
 */
public interface BeanAltered {


    Object before();
    Object after();
    boolean altered();
}
