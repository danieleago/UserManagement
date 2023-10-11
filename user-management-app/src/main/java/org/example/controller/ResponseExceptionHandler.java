package org.example.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.api.model.ErrorResponse;
import org.example.model.I18nException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ResponseExceptionHandler.class);

    @ExceptionHandler(I18nException.class)
    public ResponseEntity<ErrorResponse> handleI18n(I18nException ex, HttpServletRequest request) {
        log.debug("handle I18n");
        ErrorResponse errorResponse = new ErrorResponse()
                .code(ex.getCode())
                .message(ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(ex.getStatus()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex, HttpServletRequest request) {
        log.debug("handle DataIntegrityViolationException");
        ErrorResponse errorResponse = new ErrorResponse()
                .code("error.argument.illegal")
                .message("Illegal argument");
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(400));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, HttpServletRequest request) {
        log.debug("handle generic exception", ex);
        ErrorResponse errorResponse = new ErrorResponse()
                .code("error.generic")
                .message("Internal Server error");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
