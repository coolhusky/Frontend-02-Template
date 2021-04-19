package minispring.support;

/**
 * @author jcwang
 */
public interface Ordered {
    int HIGHEST_PRIORITY = Integer.MIN_VALUE;
    int LOWEST_PRIORITY = Integer.MAX_VALUE;

    int getOrder();
}
