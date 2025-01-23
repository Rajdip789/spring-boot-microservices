package com.rajdip14.ecommerce.handler;

import com.rajdip14.ecommerce.exception.BusinessException;
import com.rajdip14.ecommerce.exception.PaymentProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<String> handle(BusinessException exp) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exp.getMessage());
    }

    @ExceptionHandler(PaymentProcessingException.class)
    public ResponseEntity<String> handle(PaymentProcessingException exp) {
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(exp.getMessage());
    }
}
