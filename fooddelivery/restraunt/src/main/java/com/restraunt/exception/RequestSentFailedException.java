package com.restraunt.exception;

public class RequestSentFailedException extends RuntimeException{

    public RequestSentFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public RequestSentFailedException(String message) {
        super(message);
    }
}
