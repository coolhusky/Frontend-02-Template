package com.github.coolhusky.gateway.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingUtils {

    private LoggingUtils(){}

    public static Logger current() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceElement currentFrame = stackTrace[2];
        return LoggerFactory.getLogger(currentFrame.getClassName());
    }
}
