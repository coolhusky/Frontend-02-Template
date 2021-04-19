package io.github.coolhusky.minispring.utils;

import io.github.coolhusky.minispring.support.Producer;
import org.apache.commons.lang3.BooleanUtils;

/**
 * @author jcwang
 */
public class AssertUtils {
    private AssertUtils(){}

    public static <ExType extends RuntimeException> void isTrue(Producer<Boolean> condition, ExType exType) {
        if(BooleanUtils.isNotTrue(condition.get())) {
            throw exType;
        }
    }
}
