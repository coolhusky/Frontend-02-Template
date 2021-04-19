package io.github.coolhusky.minispring.aop;

/**
 * @author jcwang
 */
public interface AspectAware {
    
    void onRegisterInfo(AspectInformation aspectInfo);

    void onRegister(Aspect aspect);
}
