package com.user.exception;

public class ServiceNotAvailableException  extends RuntimeException {
    public ServiceNotAvailableException() {
        super("Service not available");
    }

    public ServiceNotAvailableException(String message) {
        super(message);
    }
}
