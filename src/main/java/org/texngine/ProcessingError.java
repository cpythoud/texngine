package org.texngine;

public class ProcessingError extends RuntimeException {

    public ProcessingError(final String message) {
        super(message);
    }

    public ProcessingError(final Throwable throwable) {
        super(throwable);
    }

}
