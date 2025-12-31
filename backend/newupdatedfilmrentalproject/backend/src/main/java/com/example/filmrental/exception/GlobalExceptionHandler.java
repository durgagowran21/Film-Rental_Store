package com.example.filmrental.exception;


//package com.example.filmstore.exception;

import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ProblemDetail> handleNotFound(NotFoundException ex, WebRequest req) {
    ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
    pd.setTitle("Not Found");
    pd.setDetail(ex.getMessage());
    pd.setProperty("instance", req.getDescription(false).replace("uri=", ""));
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(pd);
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ProblemDetail> handleBadRequest(BadRequestException ex, WebRequest req) {
    ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
    pd.setTitle("Bad Request");
    pd.setDetail(ex.getMessage());
    pd.setProperty("instance", req.getDescription(false).replace("uri=", ""));
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(pd);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ProblemDetail> handleValidation(MethodArgumentNotValidException ex, WebRequest req) {
    String msg = ex.getBindingResult().getFieldErrors().stream()
      .map(e -> e.getField() + ": " + e.getDefaultMessage())
      .findFirst().orElse("Validation error");
    ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
    pd.setTitle("Validation Error");
    pd.setDetail(msg);
    pd.setProperty("instance", req.getDescription(false).replace("uri=", ""));
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(pd);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ProblemDetail> handleConstraint(ConstraintViolationException ex, WebRequest req) {
    String msg = ex.getConstraintViolations().stream().findFirst()
      .map(v -> v.getPropertyPath() + ": " + v.getMessage())
      .orElse("Validation error");
    ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
    pd.setTitle("Constraint Violation");
    pd.setDetail(msg);
    pd.setProperty("instance", req.getDescription(false).replace("uri=", ""));
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(pd);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ProblemDetail> handleGeneric(Exception ex, WebRequest req) {
    ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    pd.setTitle("Internal Server Error");
    pd.setDetail("An unexpected error occurred");
    pd.setProperty("instance", req.getDescription(false).replace("uri=", ""));
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(pd);
  }
}
