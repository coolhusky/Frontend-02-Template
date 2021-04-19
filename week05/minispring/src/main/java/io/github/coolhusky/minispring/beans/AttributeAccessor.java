package io.github.coolhusky.minispring.beans;

/**
 * @author jcwang
 */
public interface AttributeAccessor {

    void setAttribute(String key, Object value);

    Object getAttribute(String key);
}
