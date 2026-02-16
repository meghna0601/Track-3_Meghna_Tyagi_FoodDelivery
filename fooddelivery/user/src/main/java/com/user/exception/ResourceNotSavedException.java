package com.user.exception;

public class ResourceNotSavedException extends RuntimeException {
    public ResourceNotSavedException() {
        super("Registration Not Successful");
    }

    public ResourceNotSavedException(String message) {
        super(message);
    }
}
