package com.priyanshu.page_pulse.exception;


import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;


@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {


        String message =
                ex.getBindingResult()
                        .getFieldError()
                        .getDefaultMessage();


        ErrorResponse error =
                new ErrorResponse(
                        LocalDateTime.now(),
                        400,
                        "Validation Failed",
                        message,
                        request.getRequestURI()
                );


        return ResponseEntity
                .badRequest()
                .body(error);
    }



    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(
            Exception ex,
            HttpServletRequest request) {


        ErrorResponse error =
                new ErrorResponse(
                        LocalDateTime.now(),
                        500,
                        "Internal Server Error",
                        ex.getMessage(),
                        request.getRequestURI()
                );


        return ResponseEntity
                .status(500)
                .body(error);
    }

}