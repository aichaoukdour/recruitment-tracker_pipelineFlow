package com.example.GestionDesEntretient.exception;

public class EntretienConflictException extends RuntimeException {

    public EntretienConflictException(String message) {
        super(message);
    }

    public EntretienConflictException(String message, Throwable cause) {
        super(message, cause);
    }
}
