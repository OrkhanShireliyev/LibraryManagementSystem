package com.company.librarymanagementsystem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> errorMessageResponseEntity(NotFoundException notFoundExc) {

        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value(), notFoundExc.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = AlreadyExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ErrorResponse> alreadyExistException(AlreadyExistException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.CONFLICT, HttpStatus.CONFLICT.value(), e.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }
}
