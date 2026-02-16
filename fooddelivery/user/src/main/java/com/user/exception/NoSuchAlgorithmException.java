package com.user.exception;

public class NoSuchAlgorithmException extends RuntimeException {

    public NoSuchAlgorithmException(String message) {
        super(message);
    }

    public NoSuchAlgorithmException() {
        super("Password cannot be encrypt or decrypt because of incorrect algorithm");
    }
}
