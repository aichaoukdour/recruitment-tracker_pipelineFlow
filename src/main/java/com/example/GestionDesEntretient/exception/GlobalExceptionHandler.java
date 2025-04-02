package com.example.GestionDesEntretient.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntretienConflictException.class)
    public ResponseEntity<String> handleEntretienConflictException(EntretienConflictException ex) {
        String errorMessage = "Un conflit a été détecté pour cet entretien.";
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

}
