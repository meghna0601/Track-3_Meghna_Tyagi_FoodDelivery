package com.restraunt.exception;

public class RestrauntNotFoundException extends RuntimeException{
    public RestrauntNotFoundException(String message) {
        super(message);
    }

    public RestrauntNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
