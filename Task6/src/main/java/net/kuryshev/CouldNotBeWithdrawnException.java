package net.kuryshev;

public class CouldNotBeWithdrawnException extends RuntimeException {
    public CouldNotBeWithdrawnException() {
    }

    public CouldNotBeWithdrawnException(String message) {
        super(message);
    }

    public CouldNotBeWithdrawnException(String message, Throwable cause) {
        super(message, cause);
    }

    public CouldNotBeWithdrawnException(Throwable cause) {
        super(cause);
    }

    public CouldNotBeWithdrawnException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
