package com.basicrud.backend.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.basicrud.backend.exceptions.ErrorMessage;
import com.basicrud.backend.exceptions.InvalidRequestDataException;
import com.basicrud.backend.exceptions.ResourceNotFoundException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(
        value = {
            MalformedJwtException.class,
            ExpiredJwtException.class,
            UnsupportedJwtException.class
        }
    )
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorMessage handleJwtExceptions(Exception e) {
        return new ErrorMessage(
            HttpStatus.UNAUTHORIZED.value(),
            new java.util.Date(),
            "The token is invalid or has expired",
            "Invalid or expired JWT token"
        );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage handleResourceNotFoundException(ResourceNotFoundException e) {
        return new ErrorMessage(
            HttpStatus.NOT_FOUND.value(),
            new java.util.Date(),
            e.getMessage(),
            "Resource not found"
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return new ErrorMessage(
            HttpStatus.BAD_REQUEST.value(),
            new java.util.Date(),
            e.getBindingResult().getAllErrors().get(0).getDefaultMessage(),
            "Invalid request data"
        );
    }

    // You can add exception handling methods here to handle specific exceptions globally
    @ExceptionHandler(InvalidRequestDataException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleInvalidRequestDataException(InvalidRequestDataException e) {
        return new ErrorMessage(
            HttpStatus.BAD_REQUEST.value(),
            new java.util.Date(),
            e.getMessage(),
            "Invalid request data"
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage handleGenericException(Exception e) {
        return new ErrorMessage(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            new java.util.Date(),
            e.getMessage(),
            "An unexpected error occurred"
        );
    } 
}
