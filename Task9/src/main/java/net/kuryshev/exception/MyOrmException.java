package net.kuryshev.exception;

public class MyOrmException extends RuntimeException {
    public MyOrmException() {
    }

    public MyOrmException(String message) {
        super(message);
    }

    public MyOrmException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyOrmException(Throwable cause) {
        super(cause);
    }

    public MyOrmException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
