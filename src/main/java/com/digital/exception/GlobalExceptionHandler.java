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


@ExceptionHandler(MethodArgumentNotValidException.class)
public ResponseEntity<Map<String, String>> handleValidationExceptions(
        MethodArgumentNotValidException ex) {

    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage())
    );

    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
}
    @ExceptionHandler(ExamNotFoundException.class)
    public ResponseEntity<?> handleExamNotFound(ExamNotFoundException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("status", HttpStatus.NOT_FOUND.value());
        error.put("error", "Exam Not Found");
        error.put("message", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(DuplicateExamScheduleException.class)
    public ResponseEntity<?> handleDuplicateExam(DuplicateExamScheduleException ex) {
        return buildErrorResponse(HttpStatus.CONFLICT, "Duplicate Exam Schedule", ex.getMessage());
    }

    @ExceptionHandler(InvalidSubmissionException.class)
    public ResponseEntity<?> handleInvalidSubmission(InvalidSubmissionException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Invalid Submission", ex.getMessage());
    }

    @ExceptionHandler(ResultAlreadyPublishedException.class)
    public ResponseEntity<?> handleResultPublished(ResultAlreadyPublishedException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Result Already Published", ex.getMessage());
    }

    private ResponseEntity<?> buildErrorResponse(HttpStatus status, String error, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", error);
        body.put("message", message);
        return new ResponseEntity<>(body, status);
    }

}
