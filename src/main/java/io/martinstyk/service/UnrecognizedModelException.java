package io.martinstyk.service;

public class UnrecognizedModelException extends RuntimeException {

    public UnrecognizedModelException(String message) {
        super(message);
    }

    public UnrecognizedModelException(String message, Throwable cause) {
        super(message, cause);
    }
}
