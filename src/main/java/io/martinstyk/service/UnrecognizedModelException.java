package io.martinstyk.service;

public class UnrecognizedModelException extends RuntimeException {
    public UnrecognizedModelException(String modelName) {
        super("Unrecognized model name: " + modelName);
    }
}
