package de.lehrcode.untier.web;

import javax.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ProblemHandler {
    @ExceptionHandler
    protected ResponseEntity<Problem> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
        return ResponseEntity.badRequest()
                             .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                             .body(Problem.of(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }
}
