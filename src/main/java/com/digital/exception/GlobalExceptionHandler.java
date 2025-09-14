package com.digital.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 🔴 Resource not found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<CustomResponse> handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        CustomResponse response = new CustomResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND,
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    // 🔴 Unauthorized
    @ExceptionHandler(CustomUnauthorizedException.class)
    public ResponseEntity<CustomResponse> handleUnauthorized(CustomUnauthorizedException ex, HttpServletRequest request) {
        CustomResponse response = new CustomResponse(
                LocalDateTime.now(),
                HttpStatus.UNAUTHORIZED,
                HttpStatus.UNAUTHORIZED.value(),
                ex.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    // 🔴 Forbidden
    @ExceptionHandler(CustomForbiddenException.class)
    public ResponseEntity<CustomResponse> handleForbidden(CustomForbiddenException ex, HttpServletRequest request) {
        CustomResponse response = new CustomResponse(
                LocalDateTime.now(),
                HttpStatus.FORBIDDEN,
                HttpStatus.FORBIDDEN.value(),
                ex.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    // 🔴 Student not found (very important!)
    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<CustomResponse> handleStudentNotFound(StudentNotFoundException ex, HttpServletRequest request) {
        CustomResponse response = new CustomResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND,
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    // 🔴 Duplicate resource
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<CustomResponse> handleDuplicateResource(DuplicateResourceException ex, HttpServletRequest request) {
        CustomResponse response = new CustomResponse(
                LocalDateTime.now(),
                HttpStatus.CONFLICT,
                HttpStatus.CONFLICT.value(),
                ex.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    // 🔴 Validation Errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    // 🔴 Catch all generic errors
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomResponse> handleGenericException(Exception ex, HttpServletRequest request) {
        CustomResponse response = new CustomResponse(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage() != null ? ex.getMessage() : "Internal server error",
                request.getRequestURI()
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
