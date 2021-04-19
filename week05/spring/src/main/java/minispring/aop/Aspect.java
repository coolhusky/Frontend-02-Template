package minispring.aop;

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
