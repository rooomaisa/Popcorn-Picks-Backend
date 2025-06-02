package com.popcornpicks.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
//
//@ControllerAdvice
//public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
//
//    private Map<String,Object> createBody(HttpStatus status, String message) {
//        Map<String,Object> body = new LinkedHashMap<>();
//        body.put("timestamp", LocalDateTime.now());
//        body.put("status", status.value());
//        body.put("error", status.getReasonPhrase());
//        body.put("message", message);
//        return body;
//    }
//
//    @ExceptionHandler(EmailAlreadyExistsException.class)
//    public ResponseEntity<Object> handleEmailExists(
//            EmailAlreadyExistsException ex,
//            WebRequest request) {
//
//        Map<String,Object> body = createBody(HttpStatus.CONFLICT, ex.getMessage());
//        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
//    }
//
//    @ExceptionHandler(ResourceNotFoundException.class)
//    public ResponseEntity<Object> handleNotFound(
//            ResourceNotFoundException ex,
//            WebRequest request) {
//
//        Map<String,Object> body = createBody(HttpStatus.NOT_FOUND, ex.getMessage());
//        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
//    }
//
//    @Override
//    protected ResponseEntity<Object> handleMethodArgumentNotValid(
//            MethodArgumentNotValidException ex,
//            HttpHeaders headers,
//            org.springframework.http.HttpStatusCode status,
//            WebRequest request) {
//
//        String message = ex.getBindingResult().getFieldErrors().stream()
//                .map(err -> err.getField() + ": " + err.getDefaultMessage())
//                .reduce((a, b) -> a + "; " + b)
//                .orElse(ex.getMessage());
//
//        Map<String,Object> body = createBody(HttpStatus.BAD_REQUEST, message);
//        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
//    }
//
//
//    @ExceptionHandler(DuplicateReviewException.class)
//    protected ResponseEntity<Object> handleDuplicateReview(
//            DuplicateReviewException ex,
//            WebRequest request) {
//
//        Map<String,Object> body = createBody(HttpStatus.CONFLICT, ex.getMessage());
//        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
//    }
//
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<Object> handleAllOthers(
//            Exception ex,
//            WebRequest request) {
//
//        Map<String,Object> body = createBody(
//                HttpStatus.INTERNAL_SERVER_ERROR,
//                "An unexpected error occurred"
//        );
//        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//}

