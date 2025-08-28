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

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<CustomResponse> resourceNotFoundException(ResourceNotFoundException e, HttpServletRequest request){
        CustomResponse response = new CustomResponse(LocalDateTime.now(), HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value(), e.getMessage(), request.getRequestURI());
        return new ResponseEntity<CustomResponse>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CustomUnauthorizedException.class)
    public ResponseEntity<CustomResponse> handleUnauthorized(CustomUnauthorizedException e, HttpServletRequest request) {
        CustomResponse response = new CustomResponse(
                LocalDateTime.now(),
                HttpStatus.UNAUTHORIZED,
                HttpStatus.UNAUTHORIZED.value(),
                e.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(CustomForbiddenException.class)
    public ResponseEntity<CustomResponse> handleForbidden(CustomForbiddenException e, HttpServletRequest request) {
        CustomResponse response = new CustomResponse(
                LocalDateTime.now(),
                HttpStatus.FORBIDDEN,
                HttpStatus.FORBIDDEN.value(),
                e.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomResponse> handleGenericException(Exception e, HttpServletRequest request) {
        CustomResponse response = new CustomResponse(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                e.getMessage() != null ? e.getMessage() : "Internal server error",
                request.getRequestURI()
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }



    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<CustomResponse> handleDuplicateResource(DuplicateResourceException e, HttpServletRequest request) {
        CustomResponse response = new CustomResponse(
                LocalDateTime.now(),
                HttpStatus.CONFLICT,
                HttpStatus.CONFLICT.value(),
                e.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<CustomResponse> handleValidationExceptions(
//            MethodArgumentNotValidException ex, HttpServletRequest request) {
//
//        String errorMessage = ex.getBindingResult()
//                .getFieldErrors()
//                .stream()
//                .map(err -> err.getDefaultMessage())
//                .findFirst()
//                .orElse("Validation failed");
//
//        CustomResponse response = new CustomResponse(
//                LocalDateTime.now(),
//                HttpStatus.BAD_REQUEST,
//                HttpStatus.BAD_REQUEST.value(),
//                errorMessage,
//                request.getRequestURI()
//        );
//
//        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//    }
@ExceptionHandler(MethodArgumentNotValidException.class)
public ResponseEntity<Map<String, String>> handleValidationExceptions(
        MethodArgumentNotValidException ex) {

    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage())
    );

    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
}
}
