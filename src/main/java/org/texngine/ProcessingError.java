package org.texngine;

public class ProcessingError extends RuntimeException {

    public ProcessingError(String message) {
        super(message);
    }

    public ProcessingError(Throwable throwable) {
        super(throwable);
    }

}
