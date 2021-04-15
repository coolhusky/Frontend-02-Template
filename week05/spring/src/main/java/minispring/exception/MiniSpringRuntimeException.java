package minispring.exception;

/**
 * @author jcwang
 */
public class MiniSpringRuntimeException extends RuntimeException {
    public MiniSpringRuntimeException() {
    }

    public MiniSpringRuntimeException(String message) {
        super(message);
    }

    public MiniSpringRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public MiniSpringRuntimeException(Throwable cause) {
        super(cause);
    }

    public MiniSpringRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
