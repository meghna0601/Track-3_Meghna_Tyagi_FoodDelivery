package com.user.exception;

public class ResourceAlreadyExistException extends RuntimeException{

    public ResourceAlreadyExistException(String message) {
        super(message);
    }

    public ResourceAlreadyExistException() {
        super("User Already Exists");
    }
}
