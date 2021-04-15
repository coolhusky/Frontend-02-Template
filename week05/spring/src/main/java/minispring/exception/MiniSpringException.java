package minispring.exception;

/**
 * @author jcwang
 */
public class MiniSpringException extends Exception {
    public MiniSpringException() {
    }

    public MiniSpringException(String message) {
        super(message);
    }

    public MiniSpringException(String message, Throwable cause) {
        super(message, cause);
    }

    public MiniSpringException(Throwable cause) {
        super(cause);
    }

    public MiniSpringException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
